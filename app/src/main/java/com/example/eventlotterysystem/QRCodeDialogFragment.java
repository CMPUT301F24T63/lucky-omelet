package com.example.eventlotterysystem;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeDialogFragment extends DialogFragment {

    private static final String ARG_HASH_CODE = "hashCodeQR";

    /**
     * Factory method to create a new instance of QRCodeDialogFragment with the event's hash code.
     * @param hashCodeQR the QR code hash string of the event
     * @return an instance of QRCodeDialogFragment
     */
    public static QRCodeDialogFragment newInstance(String hashCodeQR) {
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HASH_CODE, hashCodeQR);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_qrcode, container, false);

        // Retrieve the QR code hash from arguments
        String hashCodeQR = getArguments().getString(ARG_HASH_CODE);

        // Find UI components
        ImageView imageViewQRCode = view.findViewById(R.id.imageViewQRCode);
        TextView textViewHash = view.findViewById(R.id.textViewHash);

        // Display hash code in the text view
        textViewHash.setText("Hash data of your QR code:\n" + hashCodeQR);

        // Generate and display QR code based on hashCodeQR
        Bitmap qrCodeBitmap = generateQRCode(hashCodeQR);
        if (qrCodeBitmap != null) {
            imageViewQRCode.setImageBitmap(qrCodeBitmap);
        }

        return view;
    }

    /**
     * Generates a QR code bitmap from the given data string.
     * @param data the data to encode in the QR code
     * @return a Bitmap representing the QR code, or null if generation fails
     */
    private Bitmap generateQRCode(String data) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Define QR code dimensions
            int width = 200;
            int height = 200;
            // Generate QR code bit matrix
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
            // Create a bitmap from the bit matrix
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
