package per.niejun.dao;

import org.apache.ibatis.annotations.Param;
import per.niejun.entity.User;

public interface UserDao {
    User isExistUser(String uname);
    User checkUser(User user);

    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}