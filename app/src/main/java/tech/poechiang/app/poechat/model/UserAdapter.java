package tech.poechiang.app.poechat.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import tech.poechiang.app.poechat.R;

public class UserAdapter extends BaseAdapter {

    private  List<User> userList;
    private LayoutInflater mInflater;//布局装载器对象
    private  int itemResId;
    public UserAdapter(Context context, @LayoutRes int itemRes, List<User> list){
        userList = list;
        itemResId = itemRes;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(itemResId, null);

            //对viewHolder的属性进行赋值
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolder.nick = (TextView) convertView.findViewById(R.id.nick);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.msg);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);




            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();


        }



        // 取出bean对象
        User user = userList.get(position);
        viewHolder.photo.setImageBitmap(user.getPhoto());
        viewHolder.nick.setText(user.getNick());
        viewHolder.msg.setText(user.getMsg());
        viewHolder.date.setText(user.getDate());
        user.onDataChanged(new Handler() {
            public void handleMessage(Message msg) {
                UserAdapter.this.notifyDataSetChanged();
            };
        });



        return convertView;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        public ImageView photo;
        public TextView nick;
        public TextView msg;
        public TextView date;
    }

}
