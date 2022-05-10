package com.app.redesocial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.redesocial.databinding.FragmentCreatPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CreatPostFragment : Fragment() {
    private lateinit var binding: FragmentCreatPostBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatPostBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        initView()

        return binding.root
    }

    private fun initView() {
        binding.postar.setOnClickListener {
            if (binding.titlePostar.text.tos().isBlank()
                || binding.descriptionPostar.text.tos().isBlank()
            ) {

                Toast.makeText(
                    requireContext(),
                    "campos obtigatórios em branco!",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {

                val title = binding.titlePostar.text.tos()
                val descricao = binding.descriptionPostar.text.tos()



                if (addPostToDatabase(title, descricao) != null) {
                    findNavController().navigate(
                        R.id.action_creatPostFragment_to_feedFragment
                    )
                }

            }
        }

        binding.imagePost.setOnClickListener {
            if (imageGaleria() != null) {
                binding.campodetexto.text = ""
            }
        }

    }

    private fun imageGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                if (data != null) {

                    binding.imagePost.setImageURI(data.data)
                    FirebaseStorageManagerPost().uploadImage(requireContext(), data.data!!)
                }

            }

        }
    }

    private fun addPostToDatabase(title: String, descricao: String) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        val User = Firebase.auth.currentUser?.uid.toString()
        dbRef.child("user").child(User).child("id").setValue(PostData(title, descricao))

    }

}