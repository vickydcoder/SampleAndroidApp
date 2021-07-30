package com.example.sampleapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mishipay.sdk.config.WebAppConfig
import com.example.sampleapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecondBT.setOnClickListener {
            startWebActivity(view, "yes", "no", "43.3178635", "-2.9764437")
        }
        binding.buttonSecond.setOnClickListener {
            startWebActivity(view, "no", "yes", "26.0742392", "-80.1527909")
        }
        binding.buttonSecondNPA.setOnClickListener {
            startWebActivity(view, "no", "no", "26.0742392", "-80.1527909")
        }
    }

    private fun startWebActivity(
        view: View,
        isBasketTransferNeeded: String,
        isPaymentAllowed: String,
        lat: String,
        long: String
    ) {
        WebAppConfig(
            view.context,
            "no",
            isBasketTransferNeeded,
            isPaymentAllowed,
            "vivek@mishipay.com",
            "ABCDEF",
            lat,
            long,
            paymentCallBack = object : ResultReceiver(
                Handler(
                    Looper.getMainLooper()
                )
            ) {
                @SuppressLint("SetTextI18n")
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    when (resultCode) {
                        WebAppConfig.forPayment -> {
                            if (!resultData!!.isEmpty) {
                                println(
                                    "Callback Result: Payment ${
                                        resultData.getString(
                                            WebAppConfig.transactionStatus
                                        )
                                    } with order id ${resultData.getString(WebAppConfig.orderId)} and amount ${
                                        resultData.getString(
                                            WebAppConfig.transactionAmount
                                        )
                                    }"
                                )
                                binding.textviewSecond.text = "Payment ${
                                    resultData.getString(
                                        WebAppConfig.transactionStatus
                                    )
                                } with order id ${resultData.getString(WebAppConfig.orderId)} and amount ${
                                    resultData.getString(
                                        WebAppConfig.transactionAmount
                                    )
                                }"
                            }
                        }
                    }
                }
            }
        ).startShoppingCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}