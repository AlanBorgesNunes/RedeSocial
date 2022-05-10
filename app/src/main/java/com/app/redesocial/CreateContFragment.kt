package com.app.redesocial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.app.redesocial.databinding.FragmentCreateContBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class CreateContFragment : Fragment() {
    private lateinit var binding: FragmentCreateContBinding
    private var auth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateContBinding.inflate(layoutInflater)
            .apply {
                viewLifecycleOwner
            }
        initView()
        return binding.root
    }

    private fun initView() {
        auth = Firebase.auth

        binding.tvJaTemConta.setColouredSpan(
            "Já tem conta? Entrar.",
            14, 21, lazy {
                ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_200
                )
            }.value
        )

        binding.tvJaTemConta.setOnClickListener {
            findNavController()
                .navigate(R.id.action_createContFragment_to_loginFragment)
        }

        binding.btnCadastrar.setOnClickListener {

            if (binding.edtEmail.text.toString().isBlank() ||
                binding.edtSenha.text.toString().isBlank()
            ) {
                Toast.makeText(
                    requireContext(),
                    "campo obrigatório em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                recupereDados(
                    binding.edtname.text.tos(),
                    binding.edtEmail.text.toString(),
                    binding.edtSenha.text.toString()
                )

            }
        }

    }

    private fun recupereDados(name: String, email: String, password: String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    auth!!.currentUser?.let {
                        addUserToDatabase(name, email, password)
                    }

                    //navegar
                    val bundle = bundleOf(
                        "NAME" to name + ","
                    )

                    findNavController().navigate(
                        R.id.action_createContFragment_to_posLoginFragment,
                        bundle
                    )


                } else {
                    Log.d("erroFF", task.exception.toString())
                    val erro = task.exception.toString()
                    errosFirebse(erro)
                }

            }
    }

    private fun addUserToDatabase(name: String, email: String, password: String) {

        dbRef = FirebaseDatabase.getInstance().getReference()
        val User = Firebase.auth.currentUser?.uid.toString()
        dbRef.child("user").child(User).setValue(User(name, email, password))

    }

    private fun errosFirebse(erro: String) {
        if (erro.contains(
                "The email address is badly formatted."
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Formato de email inválido!",
                Toast.LENGTH_LONG
            ).show()

        }

        if (erro.contains(
                "The password is invalid or the user does not have a password."
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Senha Incorreta!",
                Toast.LENGTH_LONG
            ).show()

        }


        if (erro.contains(
                "There is no user record corresponding to this identifier"
            )
        ) {
            Toast.makeText(
                requireContext(),
                "e-mail não cadastrado na base de dados!",
                Toast.LENGTH_LONG
            ).show()

        }

        if (erro.contains(
                "The email address is already in use by another account"
            )
        ) {
            Toast.makeText(
                requireContext(),
                "e-mail já está sendo usado!",
                Toast.LENGTH_LONG
            ).show()
        }

    }

}