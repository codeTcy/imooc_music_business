package com.tcy.music_business.model.user;


import com.tcy.music_business.model.BaseModel;

/**
 * 用户真正实体数据
 */
public class UserContent extends BaseModel {

  public String userId; //用户唯一标识符
  public String photoUrl;
  public String name;
  public String tick;
  public String mobile;
  public String platform;
}
