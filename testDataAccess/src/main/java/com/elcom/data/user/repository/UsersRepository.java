package com.elcom.data.user.repository;

import org.hibernate.Session;
import com.elcom.data.BaseRepository;
import com.elcom.data.repository.IUpsertRepository;
import com.elcom.data.user.entity.User;
import com.elcom.data.user.entity.UserCompany;
import com.elcom.util.security.Protector;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

public class UsersRepository extends BaseRepository implements IUpsertRepository<User> {

    public UsersRepository(Session session) {
        super(session);
    }

    public void update(User item) {
        this.session.update(item);
    }

    public void upsert(User item) {
        this.session.saveOrUpdate("User", item);
    }

    public boolean insertUserCompany(UserCompany item) {
        this.session.save("UserCompany", item);
        return true;
    }

    @SuppressWarnings("deprecation")
    public User findUser(String accountName, String password, String loginType) {

        User user = null;

        try {
            @SuppressWarnings("rawtypes")
            NativeQuery query = this.session.getNamedNativeQuery("login");
            query.setParameter("accountName", accountName.trim());
            query.addScalar("id", StandardBasicTypes.LONG);
            query.addScalar("email", StandardBasicTypes.STRING);
            query.addScalar("password", StandardBasicTypes.STRING);
            query.addScalar("saltValue", StandardBasicTypes.STRING);
            query.addScalar("fullName", StandardBasicTypes.STRING);
            query.addScalar("mobile", StandardBasicTypes.STRING);
            query.addScalar("skype", StandardBasicTypes.STRING);
            query.addScalar("facebook", StandardBasicTypes.STRING);
            query.addScalar("avatar", StandardBasicTypes.STRING);
            query.addScalar("userType", StandardBasicTypes.INTEGER);
            query.addScalar("status", StandardBasicTypes.INTEGER);
            query.addScalar("createdAt", StandardBasicTypes.TIMESTAMP);
            query.addScalar("lastLogin", StandardBasicTypes.TIMESTAMP);
            query.addScalar("uuid", StandardBasicTypes.STRING);
            query.addScalar("address", StandardBasicTypes.STRING);
            query.addScalar("companyId", StandardBasicTypes.LONG);
            query.addScalar("companyName", StandardBasicTypes.STRING);
            query.addScalar("careerId", StandardBasicTypes.LONG);
            query.addScalar("companyType", StandardBasicTypes.INTEGER);
            query.setResultTransformer(Transformers.aliasToBean(User.class));

            user = (User) query.uniqueResult();

            if (user != null && Protector.isMatch(password.trim(), user.getPassword(), user.getSaltValue())) {
                if ("CONTINUE-CHECK".equals(loginType)) {
                    //Neu dang nhap cty hoac nha tuyen dung ==> Bo qua user
                    if (user.getUserType() == 3) {
                        return null;
                    }
                } else {
                    //Neu dang nhap user (NORMAL) ==> Bo qua nha tuyen dung
                    if (user.getUserType() == 2) {
                        return null;
                    }
                }

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
