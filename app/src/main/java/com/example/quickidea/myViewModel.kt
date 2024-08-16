package com.example.quickidea

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class myViewModel: ViewModel() {
    data class TheIdea(var title:String, val description:String){
        constructor(): this("No Title","No Description")
    }
    data class encryptedIdea(var encrpytedData:String, val ivBase64:String){
        constructor(): this("","")
    }

    private val database = FirebaseDatabase.getInstance().reference
    private val user = Firebase.auth.currentUser


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveIdea(ideaTitle: String, ideaDescription:String)
    {
        if(user!=null) {
            val userID = user.uid
            val usersRef = database.child("USERS")      // users -> userID -> Ideas -> key -> Idea
            val userXref = usersRef.child(userID)
            val key = userXref.push().key ?: return     // key will be generated in order with each seprate user
            val idea = TheIdea(ideaTitle,ideaDescription)

            val alias = "MySecretKeyAlias"
            // Generate and store the key if it doesn't exist
            var secretKey = getKey(alias)
            if (secretKey == null) {

                secretKey = generateAndStoreKey(alias)
            }
            val serializedObject = serializeObject(idea)
            val encryptedIdea = encryptData(serializedObject,secretKey)
            val encrpytedData = encryptedIdea.first
            val ivBase64 = encryptedIdea.second

            val safeIdea = encryptedIdea(encrpytedData,ivBase64)

            usersRef.child(userID).child("Ideas").child(key).setValue(safeIdea)

            Log.d("IdeaApp", "Idea saved Successfully")
        }
        else{
            Log.d("QI", "Error user not found")
        }

    }

    fun viewIdeas(ideasList: MutableList<TheIdea>)
    {
        if(user!=null) {
            val userID = user.uid
            val usersRef = database.child("USERS")      // users -> userID -> Ideas -> key -> Idea
            val userXref = usersRef.child(userID).child("Ideas")

            val alias = "MySecretKeyAlias"

            // Generate and store the key if it doesn't exist
            var secretKey = getKey(alias)
            if (secretKey == null) {
                secretKey = generateAndStoreKey(alias)
            }

            userXref.addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    ideasList.clear()
                    for (ideaSnapshot in snapshot.children) {
                        val encryptedIdea = ideaSnapshot.getValue(encryptedIdea::class.java)
                        if (encryptedIdea != null) {
                            val decryptedString = decryptData(encryptedIdea.encrpytedData,encryptedIdea.ivBase64,secretKey)
                            val idea = deserializeObject(decryptedString)
                            ideasList.add(idea)
                            Log.d("IdeaApp", "Ideas Retrieved Successfully")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    // Function to handle user sign-up
    fun signUp(firebaseAuth: FirebaseAuth,email: String, password: String,OnSuccess:()->Unit,  OnSameEmail:() -> Unit, onWrongEmail: () -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val user = firebaseAuth.currentUser
                val verified = mutableStateOf(false)
                if (task.isSuccessful) {
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { emailSent ->
                                if (emailSent.isSuccessful) {
                                    OnSuccess()

                                } else {
                                    println("Email not sent")
                                }
                            }
                        println("Email sent")
                    }
                    firebaseAuth.signOut()

                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    Log.d("QI : CreateAccount ", "Same email and password")
                    OnSameEmail()
                } else {
                    onWrongEmail()
                    Log.d("QI : CreateAccount", "sign up Error")
                }
            }
    }



    // Function to handle user sign-in
    fun signIn(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        navController: NavController,
        onSuccess: ()->Unit,
        verificationError: ()->Unit,
        verifyEmail: () -> Unit,
        onWrongEmail:() ->Unit,
        onWrongPassword: () -> Unit,

    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                            Log.d("QI : Sign IN", "sign in success")
                            onSuccess()
                            navController.navigate("home_screen")
                        } else {
                            println("Verify email")
                            verificationError()
                            user.sendEmailVerification()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        firebaseAuth.signOut() // Sign out to prevent unverified access
                                    } else {
                                        println("ERROR Sending email: ${it.exception}")
                                    }
                                }
                        }
                    }
                } else {
                    try {
                        throw task.exception ?: Exception("Unknown error")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        if (e.errorCode == "ERROR_INVALID_EMAIL") {
                            println("Bad email format")
                            onWrongEmail()
                        } else {
                            println("Incorrect EMail/Password")
                            onWrongPassword()
                        }
                    } catch (e: Exception) {
                        println("Error signing in the user: ${e.localizedMessage}")
                        onWrongEmail() // Call onWrongEmail for any other errors
                    }
                }
            }
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Password reset email sent to $email, \n\nIf you don't receive an email, means you don't have an account. Please create new account!")
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

}