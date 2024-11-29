package com.example.planetze86;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShoppingTracking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingconsumption_tracking);

        // Button references
        Button buyNewClothesButton = findViewById(R.id.button_buy_new_clothes);
        Button buyElectronicsButton = findViewById(R.id.button_buy_electronics);
        Button otherPurchasesButton = findViewById(R.id.button_other_purchases);
        Button energyBillsButton = findViewById(R.id.button_energy_bills);

        // Dialog for Buying New Clothes
        buyNewClothesButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ShoppingTracking.this);
            dialog.setContentView(R.layout.activity_new_clothing_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            EditText etClothes = dialog.findViewById(R.id.et_clothes);
            Button btnSaveClothes = dialog.findViewById(R.id.btn_save_clothes);

            btnSaveClothes.setOnClickListener(view -> {
                String clothesCountStr = etClothes.getText().toString();

                if (clothesCountStr.isEmpty()) {
                    Toast.makeText(ShoppingTracking.this, "Please enter the number of items", Toast.LENGTH_SHORT).show();
                } else {
                    int clothesCount = Integer.parseInt(clothesCountStr);
                    Toast.makeText(ShoppingTracking.this, "Saved: " + clothesCount + " clothing items", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Dialog for Buying Electronics
        buyElectronicsButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ShoppingTracking.this);
            dialog.setContentView(R.layout.activity_new_electronics_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            AutoCompleteTextView actvDeviceType = dialog.findViewById(R.id.actv_device_type);
            EditText etDevices = dialog.findViewById(R.id.et_devices);
            Button btnSaveElectronics = dialog.findViewById(R.id.btn_save_electronics);

            // Populate dropdown for device type
            String[] deviceTypes = {"Smartphone", "Laptop", "Tablet", "Television"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShoppingTracking.this, android.R.layout.simple_dropdown_item_1line, deviceTypes);
            actvDeviceType.setAdapter(adapter);
            actvDeviceType.setOnClickListener(w -> actvDeviceType.showDropDown());

            btnSaveElectronics.setOnClickListener(view -> {
                String deviceType = actvDeviceType.getText().toString();
                String devicesCountStr = etDevices.getText().toString();

                if (deviceType.isEmpty() || devicesCountStr.isEmpty()) {
                    Toast.makeText(ShoppingTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int devicesCount = Integer.parseInt(devicesCountStr);
                    Toast.makeText(ShoppingTracking.this, "Saved: " + devicesCount + " " + deviceType + " devices", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        // Dialog for Other Purchases
        otherPurchasesButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ShoppingTracking.this);
            dialog.setContentView(R.layout.activity_other_purchases_logging);

            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            AutoCompleteTextView actvPurchaseType = dialog.findViewById(R.id.actv_purchase_type);
            EditText etPurchases = dialog.findViewById(R.id.et_purchases);
            Button btnSaveOther = dialog.findViewById(R.id.btn_save_other);

            // Populate dropdown for purchase type
            String[] purchaseTypes = {"Furniture", "Appliances", "Miscellaneous"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShoppingTracking.this, android.R.layout.simple_dropdown_item_1line, purchaseTypes);
            actvPurchaseType.setAdapter(adapter);
            actvPurchaseType.setOnClickListener(w -> actvPurchaseType.showDropDown());

            btnSaveOther.setOnClickListener(view -> {
                String purchaseType = actvPurchaseType.getText().toString();
                String purchasesCountStr = etPurchases.getText().toString();

                if (purchaseType.isEmpty() || purchasesCountStr.isEmpty()) {
                    Toast.makeText(ShoppingTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int purchasesCount = Integer.parseInt(purchasesCountStr);
                    Toast.makeText(ShoppingTracking.this, "Saved: " + purchasesCount + " " + purchaseType + " purchases", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        energyBillsButton.setOnClickListener(v -> {
            // Create a custom dialog
            Dialog dialog = new Dialog(ShoppingTracking.this);
            dialog.setContentView(R.layout.activity_energy_bills);

            // Set dialog width and height
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

            // Access elements in the dialog
            AutoCompleteTextView actvBillType = dialog.findViewById(R.id.actv_bill_type);
            EditText etBillAmount = dialog.findViewById(R.id.et_bill_amount);
            Button btnSaveBill = dialog.findViewById(R.id.btn_save_energy_bill);

            // Populate dropdown for bill type
            String[] billTypes = {"Gas", "Electricity", "Water"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShoppingTracking.this, android.R.layout.simple_dropdown_item_1line, billTypes);
            actvBillType.setAdapter(adapter);
            actvBillType.setOnClickListener(w -> actvBillType.showDropDown());

            // Save button logic
            btnSaveBill.setOnClickListener(view -> {
                String billType = actvBillType.getText().toString();
                String billAmountStr = etBillAmount.getText().toString();

                if (billType.isEmpty() || billAmountStr.isEmpty()) {
                    Toast.makeText(ShoppingTracking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    double billAmount = Double.parseDouble(billAmountStr);
                    Toast.makeText(ShoppingTracking.this, "Saved: " + billType + " bill of $" + billAmount, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

    }
}
