package com.example.quickidea

import android.os.Build
import com.google.gson.Gson
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.quickidea.myViewModel
import java.util.Base64
import java.security.KeyStore
import javax.crypto.spec.GCMParameterSpec

// Generate a new AES key and store it in the Android Keystore
fun generateAndStoreKey(alias: String): Key {
    Log.d("IdeaApp", "generating key...")
    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
    keyGenerator.init(keyGenParameterSpec)
    Log.d("IdeaApp", "Key generated Successfully")
    return keyGenerator.generateKey()
}

// Retrieve the key from the Android Keystore
fun getKey(alias: String): Key? {
    Log.d("IdeaApp", "getting key")
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    return keyStore.getKey(alias, null)
}

// Encrypt the data using the provided key
@RequiresApi(Build.VERSION_CODES.O)
fun encryptData(data: String, secretKey: Key): Pair<String, String> {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)

    // Get the IV used during encryption
    val iv = cipher.iv
    val gcmSpec = GCMParameterSpec(128, iv)  // 128-bit authentication tag length

    // Encrypt the data
    val encryptedData = cipher.doFinal(data.toByteArray())

    // Encode IV and encrypted data to Base64 to store as a string
    val encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData)
    val ivBase64 = Base64.getEncoder().encodeToString(iv)

    return Pair(encryptedDataBase64, ivBase64)
}

// Decrypt the data using the provided key
@RequiresApi(Build.VERSION_CODES.O)
fun decryptData(encryptedData: String, ivBase64: String, secretKey: Key): String {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    // Decode the IV from Base64
    val iv = Base64.getDecoder().decode(ivBase64)
    val gcmSpec = GCMParameterSpec(128, iv)  // 128-bit authentication tag length

    // Initialize cipher with decryption mode and GCMParameterSpec
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

    // Decode and decrypt the data
    val decodedEncryptedData = Base64.getDecoder().decode(encryptedData)
    val decryptedData = cipher.doFinal(decodedEncryptedData)

    return String(decryptedData)
}


fun serializeObject(theIdea: myViewModel.TheIdea): String {
    val gson = Gson()
    return gson.toJson(theIdea)
}

// Convert JSON string back to object
fun deserializeObject(jsonData: String): myViewModel.TheIdea {
    val gson = Gson()
    return gson.fromJson(jsonData, myViewModel.TheIdea::class.java)
}
