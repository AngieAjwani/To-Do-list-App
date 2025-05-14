package com.example.to_dolist.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

object NavigationUtils {

    fun Fragment.navigateTo(destinationId: Int, args: Bundle? = null) {
        findNavController().navigate(destinationId, args)
    }

    fun NavController.safeNavigate(destinationId: Int, args: Bundle? = null) {
        currentDestination?.getAction(destinationId)?.let {
            navigate(destinationId, args)
        }
    }

    fun Fragment.popBackStack() {
        findNavController().popBackStack()
    }
}