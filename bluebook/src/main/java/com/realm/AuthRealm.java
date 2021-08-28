package com.realm;

import com.pojo.Role;
import com.pojo.User;
import com.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: qiang
 * @ProjectName: adminsystem
 * @Package: com.qiang.shiro
 * @Description: shiro认证授权
 * @Date: 2019/6/20 0020 13:02
 **/
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<String> roleNameList = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            for (Role role:
                    roles) {
                roleNameList.add(role.getName());
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roleNameList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String phone = usernamePasswordToken.getUsername();
        User user = userService.findUserByPhone(phone);
        if (user == null) {
            throw new UnknownAccountException("该手机号不存在！");
        } else {
            String realmName = getName();
            ByteSource credentialsSalt = ByteSource.Util.bytes(user.getPhone());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, realmName);
            return info;
        }
    }
}
