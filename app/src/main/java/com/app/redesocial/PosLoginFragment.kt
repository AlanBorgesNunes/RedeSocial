package com.app.redesocial

import android.app.Activity
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.INTERNAL_CONTENT_URI
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.app.redesocial.databinding.FragmentPosLoginBinding
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class PosLoginFragment : Fragment() {
    private lateinit var binding: FragmentPosLoginBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPosLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        initView()
        return binding.root
    }

    private fun initView() {
        val name = requireArguments().getString("NAME")
        binding.nameRecebe.text = name

        binding.colocarImagemPerfil.setOnClickListener {
            imageGaleria()
        }

        binding.continuar.setOnClickListener {
            if (binding.descPosLogin.text.tos().isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Campos obrigatórios em branco!",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val bundle = bundleOf(
                    "NAME" to name
                )

                addToDataBase(name.toString(), binding.descPosLogin.text.tos())


                findNavController().navigate(
                    R.id.action_posLoginFragment_to_profileFragment
                )

            }
        }
    }


    private fun addToDataBase(name: String, description: String) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        val id = Firebase.auth.currentUser?.uid.tos()
        dbRef.child("user").child(id).child("descrição")
            .setValue(Description(name, description))

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

                    binding.colocarImagemPerfil.setImageURI(data.data)
                    FirebaseStorageManager().uploadImage(requireContext(), data.data!!)
                }


            }

        }
    }
}