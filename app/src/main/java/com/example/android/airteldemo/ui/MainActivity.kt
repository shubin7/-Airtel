package com.example.android.airteldemo.ui

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.airteldemo.R
import com.example.android.airteldemo.isConnected
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    // viewModel that contains the business logic and states for this activity
    private val mainVM by lazy { ViewModelProviders.of(this).get(MainVM::class.java) }
    // Adapter for the KeyActions recyclerView
    private val keyActionsRvAdapter by lazy {
        RvIssuesAdapter { keyAction ->
            keyAction.keyCode?.let { keyCode ->


                // Request for CALL_PHONE permission
                val permRequest = permissionsBuilder(Manifest.permission.CALL_PHONE).build()
                permRequest.listeners {

                    onAccepted {
                        // When accepted, make call to the keyCode provided
                        makeCall(keyCode)
                    }

                    onDenied {
                        // Show toast to user when the permissions are denied
                        toast("Call phone permission not granted")
                    }
                }

                // Send forward the request to system
                permRequest.send()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        // Setting custom toolbar as the supportActionBar
        setSupportActionBar(toolbar)

        // Enable and set the back button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }


        // RecyclerView Initialization
        rvKeyActions.layoutManager = LinearLayoutManager(this)
        // Makes rendering faster if the recyclerView item size is not dynamic
        rvKeyActions.setHasFixedSize(true)
        // Adds a divider between each RV item
        rvKeyActions.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        // Set the adapter
        rvKeyActions.adapter = keyActionsRvAdapter
    }

    override fun onStart() {
        super.onStart()

        checkConnectionAndLoadData()
    }

    private fun checkConnectionAndLoadData() {

        if (isConnected()) {
            progressBar.visibility = View.VISIBLE
            rvKeyActions.visibility = View.VISIBLE
            flNoInternet.visibility = View.GONE
            loadAllCheckpoints()
        } else {
            //toast("No internet")
            flNoInternet.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            btnRetry.setOnClickListener {
                checkConnectionAndLoadData()
            }
        }
    }

    private fun loadAllCheckpoints() {

        // Fetch and observe the liveData from MainVM
        mainVM.getKeyActionsLd().observe(this, Observer {

            // When a new value is available, set it to the recyclerView adapter
            keyActionsRvAdapter.setKeyActions(it)
            progressBar.visibility = View.GONE
            rvKeyActions.visibility = View.VISIBLE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu for this activity
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
