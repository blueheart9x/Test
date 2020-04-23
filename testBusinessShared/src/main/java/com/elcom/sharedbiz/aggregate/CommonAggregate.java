package com.elcom.sharedbiz.aggregate;

import com.elcom.data.UnitOfWork;
import com.elcom.data.interview.entity.Rating;
import com.elcom.data.user.entity.User;
import com.elcom.sharedbiz.validation.AuthorizationException;

public class CommonAggregate {

    private UnitOfWork _uok = null;

    public CommonAggregate(UnitOfWork uok) {
        this._uok = uok;
    }

    public boolean insertRating(Rating item) {
        return this._uok.vi.commonRepository().insertRating(item);
    }
    
    public User login(String accountName, String password, String loginType) throws AuthorizationException {
        User user = null;
        try {
            user = this._uok.user.usersRepository().findUser(accountName, password, loginType);
        } catch (Throwable e) {
            throw new AuthorizationException("AccountName or Password is invalid.");
        }
        return user;
    }
}
