package per.niejun.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.niejun.dao.UserDao;
import per.niejun.entity.User;
import per.niejun.service.UserService;


/**
 * Created by 8000115097 聂钧 on 2019-03-31.
 */
@Service//加上@Service注解，让spring能够扫到。
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public boolean login(User user){
        User temp = userDao.checkUser(user);

        return temp == null ? false : true;
    }
}
