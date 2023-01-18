package com.lafierage.test.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcV
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lafierage.test.nfc.databinding.ActivityMainBinding
import java.nio.charset.Charset
import kotlin.experimental.and

class MainActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    val binding: ActivityMainBinding
        get() = _binding!!
    private var _binding: ActivityMainBinding? = null

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent

    private val intentFiltersArray = arrayOf(
        IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)!!
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(
                this,
                this.javaClass,
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0,
        )
        nfcAdapter.enableReaderMode(this, { tag ->
            Toast.makeText(this, "Tag discovered : ${tag.id.toHex()}", Toast.LENGTH_LONG).show()
        }, NfcAdapter.FLAG_READER_NFC_V, null)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(
            this,
            pendingIntent,
            intentFiltersArray,
            null,
        )
        readFromIntent(intent)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    private fun readFromIntent(intent: Intent) {
        if (
            NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
        ) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
                .map { it as NdefMessage }
                .let { buildTagView(it)}
        }
    }

    private fun buildTagView(messages: List<NdefMessage>) {
        val payload = messages[0].records[0].payload
        val textEncoding = if ((payload[0] and 128.toByte()).toInt() == 0) "UTF-8" else "UTF-16"
        val languageCodeLength = payload[0] and 63.toByte()
        val text = String(payload, languageCodeLength+1, payload.size - languageCodeLength, Charset.forName(textEncoding))
        binding.lblRecNumber.text = "Nfc Content : $text"
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onTagDiscovered(tag: Tag?) {
        if (tag != null)  Toast.makeText(this, "Tag discovered : ${tag.id}", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Reading error", Toast.LENGTH_LONG).show()
    }
}