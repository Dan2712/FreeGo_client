package com.freego.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationSet;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by henryye on 2/28/16.
 */
public class MyListView extends ListView{
    private int actionDownY;
    private TextView headerImage;
    private RelativeLayout searchAndCircle;
    private RelativeLayout searchAndListview;
    private AnimationSet animationSet;



/**
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        headerImage = (TextView)getRootView().findViewById(R.id.locationText);
        searchAndCircle = (RelativeLayout)getRootView().findViewById(R.id.hotelPage_circleAndSearch);
        int searchBarTop = searchAndCircle.getTop();
        int headerHeight = headerImage.getBottom();
        int searchbarHeight = searchAndCircle.getHeight();
        int listViewTop = this.getTop();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDownY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getRawY();
                int dy = y - actionDownY;
                if(dy > 108){
                    dy = 108;
                }
                int linearTop = searchAndListview.getTop();
                int linearBottom = searchAndListview.getBottom();
                int searchBarBottom = searchAndCircle.getBottom();

                Log.d("Original searchBarTop ", ""+searchBarTop);
                if (linearTop <= headerHeight & linearTop >= 0) {
                    final View topView = this.getChildAt(0);
                    if(dy > 0 & topView.getTop() == 0) {           //这个是下拉      改成一个if条件， 让listview 和 总的layout一起动， 当超过half height时， 则listView停止动
                        if (linearTop >= 0 & listViewTop <= (searchbarHeight/2) & dy < searchbarHeight / 2) {
                            if(dy > 59){
                                dy = 59;
                            }
                            this.setTop(this.getTop() + dy);
                            this.setBottom(this.getBottom() + dy);
                          //  topView.setTop(0);
                            }
                        else if ((linearTop + listViewTop + dy) > 0 & (linearTop + listViewTop+dy) < headerHeight) {
                            searchAndListview.setTop(linearTop + dy);
                            searchAndListview.setBottom(linearBottom + dy);
                           // topView.setTop(0);
                            }
                    }

                    else if(dy < 0 ){                                           //然而这个是上\
                        if(linearTop +dy <0 & listViewTop + dy> 0 & linearTop >= 0){
                            this.setTop(this.getTop() + dy);
                            this.setBottom(this.getBottom() + dy);
                            if(this.getTop() <= 2 | linearTop <= 2){                           //防止出现顶部出现微小空隙
                                this.setTop(0);
                                searchAndListview.setTop(0);
                            }

                        }
                        else if ((linearTop+dy) > 0 ) {
                            searchAndListview.setTop(linearTop + dy);
                            searchAndListview.setBottom(linearBottom + dy);
                            if(this.getTop() <= 2 | linearTop <= 2){
                                this.setTop(0);
                                searchAndListview.setTop(0);
                            }
                            Log.d("FirstP 2 ", "" + this.getFirstVisiblePosition());
                        }
                    }
                    actionDownY = (int) ev.getRawY();
                    Log.d("Height ", ""+searchbarHeight/2 );
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    **/


    /**
     @Override
    public boolean onTouchEvent(MotionEvent ev) {
    headerImage = (TextView)getRootView().findViewById(R.id.locationText);
    searchAndCircle = (RelativeLayout)getRootView().findViewById(R.id.hotelPage_circleAndSearch);
    searchAndListview = (RelativeLayout)getRootView().findViewById(R.id.homepage_drag_framelayout);
    int searchBarTop = searchAndCircle.getTop();
    int headerHeight = headerImage.getBottom();
    int searchbarHeight = searchAndCircle.getHeight();
    int listViewTop = this.getTop();

    switch (ev.getAction()) {
    case MotionEvent.ACTION_DOWN:
    actionDownY = (int)ev.getRawY();
    break;
    case MotionEvent.ACTION_MOVE:
    int y = (int) ev.getRawY();
    int dy = y - actionDownY;
    int linearTop = searchAndListview.getTop();
    int linearBottom = searchAndListview.getBottom();
    int searchBarBottom = searchAndCircle.getBottom();

    Log.d("Original searchBarTop ", ""+searchBarTop);
    if (linearTop <= headerHeight & linearTop >= 0) {
    final View topView = this.getChildAt(0);
    if(dy > 0 & topView.getTop() == 0) {           //这个是下拉      改成一个if条件， 让listview 和 总的layout一起动， 当超过half height时， 则listView停止动
    if(linearTop >= 0 &  listViewTop <= (searchbarHeight/2) & dy < searchbarHeight/2) {
    this.setTop(this.getTop() + dy);
    this.setBottom(this.getBottom() + dy);
    Log.d("dydydydy ", ""+ dy);
    Log.d("sdasdasd ", " " + this.getTop());
    Log.d("linearTop ", ""+linearTop);
    Log.d("searchBarTop", searchBarTop+"");
    }
    else if ((linearTop + listViewTop+dy) > 0 & (linearTop + listViewTop+dy) < headerHeight) {
    searchAndListview.setTop(linearTop + dy);
    searchAndListview.setBottom(linearBottom + dy);

    }
    }
    else if(dy < 0 ){                                           //然而这个是上\

    if(linearTop +dy <0 & listViewTop + dy> 0 & linearTop >= 0){
    this.setTop(this.getTop() + dy);
    this.setBottom(this.getBottom() + dy);
    }
    else if ((linearTop+dy) > 0 ) {
    searchAndListview.setTop(linearTop + dy);
    searchAndListview.setBottom(linearBottom + dy);
    Log.d("FirstP 2 ", "" + this.getFirstVisiblePosition());
    }
    Log.d("linearTop--", ""+linearTop);
    Log.d("ListVIEWTop--", ""+ listViewTop);
    Log.d("dydy", ""+dy);
    }
    actionDownY = (int) ev.getRawY();
    }

    break;
    case MotionEvent.ACTION_UP:
    break;
    default:
    break;
    }
    return super.onTouchEvent(ev);
    }
    **/


    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
