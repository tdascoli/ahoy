package com.apollo29.ahoy.view.scanning;

public interface QRCodeFoundListener {
    void qrCodeFound(String qrCode);
    void qrCodeNotFound();
}
