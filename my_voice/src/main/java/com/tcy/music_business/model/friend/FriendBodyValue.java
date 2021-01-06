package com.tcy.music_business.model.friend;


import com.tcy.lib_audio.mediaplayer.model.AudioBean;
import com.tcy.music_business.model.BaseModel;

import java.util.ArrayList;

/**
 * @文件描述：朋友实体
 */
public class FriendBodyValue extends BaseModel {

  public int type;
  public String avatr;
  public String name;
  public String fans;
  public String text;
  public ArrayList<String> pics;
  public String videoUrl;
  public String zan;
  public String msg;
  public AudioBean audioBean;
}
