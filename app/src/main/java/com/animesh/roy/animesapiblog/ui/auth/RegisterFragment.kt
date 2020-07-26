package com.animesh.roy.animesapiblog.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.animesh.roy.animesapiblog.R
import com.animesh.roy.animesapiblog.util.GenericApiResponse


class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "RegisterFragment: ${viewModel.hashCode()}")


        viewModel.testRegister().observe(viewLifecycleOwner, Observer { response ->
            when(response) {

                is GenericApiResponse.ApiSuccessResponse -> {
                    Log.d(TAG, "REGISTRATION RESPONSE: ${response.body} ")
                }

                is GenericApiResponse.ApiErrorResponse -> {
                    Log.d(TAG, "REGISTRATION RESPONSE: ${response.errorMessage} ")
                }

                is GenericApiResponse.ApiEmptyResponse -> {
                    Log.d(TAG, "REGISTRATION RESPONSE: Empty Response ")
                }
            }
        })
    }

}
