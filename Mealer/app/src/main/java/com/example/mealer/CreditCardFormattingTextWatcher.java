package com.example.mealer;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CreditCardFormattingTextWatcher implements TextWatcher {
    private EditText etCard;
    boolean isDelete;

    public CreditCardFormattingTextWatcher(EditText etcard) {
        this.etCard=etcard;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(before==0)
            isDelete=false;
        else
            isDelete=true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        String source = s.toString();
        int length=source.length();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(source);

        if(length>0 && length%5==0)
        {
            if(isDelete)
                stringBuilder.deleteCharAt(length-1);
            else
                stringBuilder.insert(length-1," ");

            etCard.setText(stringBuilder);
            etCard.setSelection(etCard.getText().length());

        }
    }


    public void afterTextChanged(Editable s) {

        String source = s.toString();
        int length = source.length();

        if (length >= 4 && creditCardType != null)
            creditCardType.setCardType(CreditCardUtils.getCardType(source.trim()));

        if (ivType != null && length == 0)
            ivType.setImageResource(android.R.color.transparent);

    }
}


public class CreditCardUtils {

    public static final String VISA_PREFIX = "4";
    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
    public static final String DISCOVER_PREFIX = "6011";
    public static final String AMEX_PREFIX = "34,37,";

    public static int getCardType(String cardNumber) {

        if (cardNumber.substring(0, 1).equals(VISA_PREFIX))
            return VISA;
        else if (MASTERCARD_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            return MASTERCARD;
        else if (AMEX_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            return AMEX;
        else if (cardNumber.substring(0, 4).equals(DISCOVER_PREFIX))
            return DISCOVER;

        return NONE;
    }
}
