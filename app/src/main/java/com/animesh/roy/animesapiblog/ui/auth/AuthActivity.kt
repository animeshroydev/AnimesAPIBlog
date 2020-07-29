package com.animesh.roy.animesapiblog.ui.auth

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.animesh.roy.animesapiblog.R
import com.animesh.roy.animesapiblog.ui.BaseActivity
import com.animesh.roy.animesapiblog.ui.ResponseType
import com.animesh.roy.animesapiblog.ui.main.MainActivity
import com.animesh.roy.animesapiblog.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class AuthActivity : BaseActivity(),
NavController.OnDestinationChangedListener
{

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewmodel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewmodel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()
    }

    fun subscribeObservers() {
        viewmodel.dataState.observe(this, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->

                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            Log.d(TAG, "AuthActivity, DataState: ${it}")
                            viewmodel.setAuthToken(it)
                        }
                    }

                }

                data.response?.let { event->
                    event.getContentIfNotHandled()?.let {
                        when(it.responseType) {
                            is ResponseType.Dialog -> {

                            }

                            is ResponseType.Toast -> {

                            }

                            is ResponseType.None -> {
                                Log.e(TAG,"AuthActivity, Response: ${it.message}")
                            }

                        }
                    }
                }
            }
        })

        viewmodel.viewState.observe(this, Observer {
            it.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer {authToken ->
            Log.d(TAG, "AuthActivity: subscribeObservers: AuthToken: ${authToken}")

            if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewmodel.cancelActiveJobs()
    }
}
