package tech.poechiang.app.poechat.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tech.poechiang.app.poechat.R;

public class SessionAdapter extends BaseAdapter {


    private List<Session> sessionList;
    private LayoutInflater mInflater;//布局装载器对象
    private  int itemResId;
    public SessionAdapter(Context context, @LayoutRes int itemRes, List<Session> list){
        sessionList = list;
        itemResId = itemRes;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return sessionList.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SessionAdapter.ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new SessionAdapter.ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(itemResId, null);

            //对viewHolder的属性进行赋值
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.mli_photo_view);
            viewHolder.nick = (TextView) convertView.findViewById(R.id.mli_nick_view);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.mli_msg_view);
            viewHolder.date = (TextView) convertView.findViewById(R.id.mli_date_view);




            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (SessionAdapter.ViewHolder) convertView.getTag();


        }



        // 取出bean对象
        Session session = sessionList.get(position);
        viewHolder.photo.setImageBitmap(session.getPhoto());
        viewHolder.nick.setText(session.getNick());
        viewHolder.msg.setText(session.getMsg());
        viewHolder.date.setText(session.getDate());
        session.onDataChanged(new Handler() {
            public void handleMessage(Message msg) {
                SessionAdapter.this.notifyDataSetChanged();
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
