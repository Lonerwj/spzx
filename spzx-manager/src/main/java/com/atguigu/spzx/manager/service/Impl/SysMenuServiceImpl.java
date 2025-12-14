package com.atguigu.spzx.manager.service.Impl;

import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.mapper.SysRoleMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.entity.system.SysRoleMenu;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.SysMenuVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单数据
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);

        if (CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }
        //构建树形菜单
        return MenuHelp(sysMenuList);
    }

    @Override
    public Boolean removeById(Long id) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        if (sysMenuMapper.selectCount(queryWrapper) > 0){
            return false;
        }else {
            sysMenuMapper.deleteById(id);
            return true;
        }
    }

    @Override
    public List<SysMenuVo> menus() {
        //获取当前用户Id
        SysUser sysUser = AuthContextUtil.get();

        //根据userId查询可以操作菜单
        List<SysMenu> sysMenuList = sysMenuMapper.findMenusByUserId(sysUser.getId());

        //构建树形菜单
        List<SysMenu> sysMenuListTree = MenuHelp(sysMenuList);

        //构建前端树形菜单
        return buildMenus(sysMenuListTree);
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }


    private List<SysMenu> MenuHelp(List<SysMenu> sysMenuList){
        List<SysMenu> treeMenuList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            //父级菜单 parent_id = 0
            if (sysMenu.getParentId() == 0L) {
                treeMenuList.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return treeMenuList;
    }

    private SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu menu : sysMenuList) {
            if (sysMenu.getId().equals(menu.getParentId())) {
                //递归到底层添加子菜单
                sysMenu.getChildren().add(findChildren(menu,sysMenuList));
            }
        }
        return sysMenu;
    }

    @Override
    public boolean save(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);

        //添加子菜单，把父菜单isHalf改为半开状态
        updateMultiHalf(sysMenu);

        return true;
    }

    private void updateMultiHalf(SysMenu sysMenu) {

        //如果存在父级菜单
        if (sysMenu.getParentId() != 0L){
            SysMenu parentMenu = sysMenuMapper.selectById(sysMenu.getParentId());
            QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("menu_id",parentMenu.getId());
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(queryWrapper);
            if (!sysRoleMenuList.isEmpty()){
                sysRoleMenuList.forEach(sysRoleMenu -> {
                    sysRoleMenu.setIsHalf(1);
                    sysRoleMenuMapper.updateById(sysRoleMenu);
                });
            }
            updateMultiHalf(parentMenu);
        }

    }

}
