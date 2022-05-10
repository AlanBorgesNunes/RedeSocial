package com.app.redesocial

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private val TAG = "FirebaseStorageManager"

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    fun uploadImage(mContext: Context, imageURI: Uri) {
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog.setMessage("Aguarde, carregando imagem...")
        mProgressDialog.show()
        val user = Firebase.auth.currentUser?.uid.tos()
        val uploadTask = mStorageRef.child(user).child("images").putFile(imageURI)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "upload success")
            val downloadURLTask = mStorageRef.child(user).child("images").downloadUrl
            downloadURLTask.addOnSuccessListener {
                Log.e(TAG, "IMAGE PATH: $it")
                mProgressDialog.dismiss()
            }.addOnFailureListener {
                mProgressDialog.dismiss()
            }
        }.addOnFailureListener {
            Log.e(TAG, "upload failed ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }

    }
}