package com.example.mealer.listener;

import com.example.mealer.model.CartModel;

import java.util.List;

public interface ICartLoadListener {

    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed (String meassage);

}
