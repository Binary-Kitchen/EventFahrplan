package nerd.tuxmobil.fahrplan.congress.net

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.extensions.withArguments
import nerd.tuxmobil.fahrplan.congress.utils.AlertDialogHelper

/**
 * Displays the given certificate error message in a dialog.
 */
class CertificateErrorFragment : DialogFragment() {

    companion object {

        private const val FRAGMENT_TAG = "CERTIFICATE_ERROR_FRAGMENT_TAG"
        private const val BUNDLE_KEY_ERROR_MESSAGE = "BUNDLE_KEY_ERROR_MESSAGE"

        @JvmStatic
        fun showDialog(fragmentManager: FragmentManager, errorMessage: String) {
            val fragment = CertificateErrorFragment().withArguments(
                    BUNDLE_KEY_ERROR_MESSAGE to errorMessage
            )
            fragment.show(fragmentManager, FRAGMENT_TAG)
        }

    }

    @NonNull
    private lateinit var errorMessage: String

    @MainThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = requireArguments()
        errorMessage = arguments.getString(BUNDLE_KEY_ERROR_MESSAGE)!!
    }

    @MainThread
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialogHelper.createErrorDialog(
                    requireContext(),
                    R.string.certificate_error_title,
                    R.string.certificate_error_message,
                    errorMessage
            )

}
