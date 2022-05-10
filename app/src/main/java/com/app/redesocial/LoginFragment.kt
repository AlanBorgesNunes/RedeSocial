package com.app.redesocial

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.redesocial.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        initView()
        return binding.root
    }


    private fun initView() {
        auth = Firebase.auth

        val user = Firebase.auth.currentUser?.uid
        if (user != null) {
            findNavController().navigate(
                R.id.action_loginFragment_to_profileFragment
            )
        }

        binding.tvCriarConta.setColouredSpan(
            "Ainda não tem conta? Criar conta",
            21, 32, lazy {
                ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_200
                )
            }.value
        )

        binding.tvCriarConta.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_createContFragment)
        }

        binding.esqueceuSenha.setOnClickListener {
            dialogRecuperarSenha()
        }

        binding.login.setOnClickListener {

            if (binding.edtEmailLogin.text.toString()
                    .isBlank() || binding.edtSenhaLogin.text.toString().isBlank()
            ) {
                Toast.makeText(
                    requireContext(),
                    "campo obrigatório em branco!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                login(
                    binding.edtEmailLogin.text.toString(),
                    binding.edtSenhaLogin.text.toString()
                )
            }
        }

    }

    private fun dialogRecuperarSenha() {
        val view = View.inflate(context, R.layout.dialg_forget_password, null)
        val username = view.findViewById<EditText>(R.id.et_username)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeDialog = view.findViewById<LinearLayout>(R.id.close_dialog)

        closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        val btn = view.findViewById<LinearLayout>(R.id.btn_enviar_email)
        btn.setOnClickListener {
            if (username.text.tos().isBlank()) {

                username.error = "Campo obrigatório em branco!"

            }


            recuperePassword(username)
        }

    }

    private fun recuperePassword(username: EditText) {

        if (username.text.tos().isEmpty()) {
            username.error = "Campo obrigatório em branco!"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.tos()).matches()) {
            return
        }

        auth?.sendPasswordResetEmail(username.text.tos())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Confira seu email para recuperar sua senha!",
                        Toast.LENGTH_LONG
                    )
                        .show()


                } else {
                    Log.d("erroF", task.exception.tos())
                    val erro = task.exception.toString()
                    errosFirebse(erro)
                }

            }
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

    private fun login(email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //navegar
                    findNavController().navigate(
                        R.id.action_loginFragment_to_profileFragment
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("erroFF", task.exception.toString())
                    val erro = task.exception.toString()
                    errosFirebse(erro)

                }
            }
    }
}