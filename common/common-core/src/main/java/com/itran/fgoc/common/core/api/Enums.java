package com.itran.fgoc.common.core.api;

import java.io.Serializable;
import java.util.*;

/**
 * @program: fgoc-cnshe
 * @description: 枚举类
 * @create: 2020-08-03 14:53
 */
public class Enums implements Serializable {

    public String name(Object id) {
        if (id == null) {
            return "";
        }
        for (Iterator<item> i = list.iterator(); i.hasNext(); ) {
            item item = i.next();
            if (item.getId().equals(id)) {
                return item.getName();
            }
        }
        return "";
    }

    public boolean idsContains(String id) {
        //传入路径的长度
        int idL = id.length();
        //配置过滤路径的长度
        int itemIdL;
        //只有长的包含短的,没有短的包含长的.如果一样长,无所谓
        for (Enums.item item : list) {
            itemIdL = item.getId().toString().length();
            if (idL <= itemIdL) {
                if (item.getId().toString().contains(id)) {
                    return true;
                }
            } else {
                if (id.contains(item.getId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<item> list = new ArrayList<item>();

    public List<item> list() {
        return this.list;
    }

    public static Enums build() {
        return new Enums();
    }

    public Enums add(Object id, String name) {
        this.list.add(new item(id, name));
        return this;
    }

    public Enums addAll(Enums enums) {
        this.list.addAll(enums.list);
        return this;
    }

    public Enums addAll(List<item> list) {
        this.list.addAll(list);
        return this;
    }

    public List<item> getList() {
        return list;
    }

    public void setList(List<item> list) {
        this.list = list;
    }

    public Map<String, String> createMap() {
        Map<String, String> map = new HashMap<>();
        for (item item : list) {
            map.put(item.getId() + "", item.getName());
        }
        return map;
    }

    public class item implements Serializable {
        public item(Object id, String name) {
            this.id = id;
            this.name = name;
        }

        private Object id;
        private String name;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
