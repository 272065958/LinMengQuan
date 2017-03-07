package net.kamfat.omengo.http;

import net.kamfat.omengo.bean.api.DatumResponse;

/**
 * Created by cjx on 2016/7/18.
 */
public interface MyCallbackInterface {
    void success(DatumResponse response);
    void error();
}
