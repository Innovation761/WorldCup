package com.ss.video.rtc.demo.quickstart;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.bytertc.engine.RTCEngine;
import com.ss.bytertc.engine.RTCRoom;
import com.ss.bytertc.engine.RTCRoomConfig;
import com.ss.bytertc.engine.RTCVideo;
import com.ss.bytertc.engine.UserInfo;
import com.ss.bytertc.engine.VideoCanvas;
import com.ss.bytertc.engine.VideoEncoderConfig;
import com.ss.bytertc.engine.data.AudioRoute;
import com.ss.bytertc.engine.data.CameraId;
import com.ss.bytertc.engine.data.RemoteStreamKey;
import com.ss.bytertc.engine.data.StreamIndex;
import com.ss.bytertc.engine.data.VideoFrameInfo;
import com.ss.bytertc.engine.handler.IRTCEngineEventHandler;
import com.ss.bytertc.engine.handler.IRTCVideoEventHandler;
import com.ss.bytertc.engine.type.ChannelProfile;
import com.ss.bytertc.engine.type.MediaStreamType;
import com.ss.rtc.demo.quickstart.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * VolcEngineRTC 视频通话的主页面
 * 本示例不限制房间内最大用户数；同时最多渲染四个用户的视频数据（自己和三个远端用户视频数据）；
 *
 * 包含如下简单功能：
 * - 创建引擎
 * - 设置视频发布参数
 * - 渲染自己的视频数据
 * - 加入音视频通话房间
 * - 切换前置/后置摄像头
 * - 打开/关闭麦克风
 * - 打开/关闭摄像头
 * - 切换听筒/扬声器
 * - 渲染远端用户的视频数据
 * - 离开房间
 * - 销毁引擎
 *
 * 实现一个基本的音视频通话的流程如下：
 * 1.创建引擎 {@link RTCEngine#create(Context, String, IRTCEngineEventHandler)}
 * 2.设置编码参数 {@link RTCEngine#setVideoEncoderConfig(List)}
 * 3.开启本地视频采集 {@link RTCEngine#startVideoCapture()}
 * 4.设置本地视频渲染视图 {@link RTCEngine#setLocalVideoCanvas(StreamIndex, VideoCanvas)}
 * 5.加入音视频通话房间 {@link RTCEngine#joinRoom(java.lang.String, java.lang.String,
 *   com.ss.bytertc.engine.UserInfo, com.ss.bytertc.engine.RTCRoomConfig)}
 * 6.在收到远端用户视频首帧之后，设置用户的视频渲染视图 {@link RTCEngine#setRemoteVideoCanvas(String, StreamIndex, VideoCanvas)}
 * 7.在用户离开房间之后移出用户的视频渲染视图 {@link RTCRoomActivity#removeRemoteView(String)}
 * 8.离开音视频通话房间 {@link RTCEngine#leaveRoom()}
 * 9.销毁引擎 {@link RTCEngine#destroyEngine(RTCEngine)}
 *
 * 有以下常见的注意事项：
 * 1.创建引擎时，需要注册 RTC 事件回调的接口，类型是 IRTCEngineEventHandler 用户需要持有这个引用，例如本示例中
 *   将其作为 Activity 成员变量。因为这个引用在引擎内部是弱引用，可能会被系统回收导致收不到回调事件
 * 2.本示例中，我们在 {@link IRTCEngineEventHandler#onFirstRemoteVideoFrameDecoded} 这个事件中给远端用户
 *   设置远端用户视频渲染视图，这个回调表示的是收到了远端用户的视频第一帧。当然也可以在
 *   {@link IRTCEngineEventHandler#onUserJoined} 回调中设置远端用户视频渲染视图
 * 3.SDK 回调不在主线程，UI 操作需要切换线程
 * 4.用户成功加入房间后，SDK 会通过 {@link IRTCEngineEventHandler#onUserJoined} 回调已经在房间的用户信息
 * 5.SDK 支持同时发布多个参数的视频流，接口是 {@link RTCEngine#setVideoEncoderConfig}
 * 6.加入房间时，需要有有效的 token，加入失败时会通过 {@link IRTCEngineEventHandler#onError(int)} 输出对应的错误码
 * 7.用户可以通过 {@link RTCEngine#joinRoom(java.lang.String, java.lang.String,
 *   com.ss.bytertc.engine.UserInfo, com.ss.bytertc.engine.RTCRoomConfig)} 中的 ChannelProfile
 *   来获得不同场景下的性能优化。本示例是音视频通话场景，因此使用 {@link ChannelProfile#CHANNEL_PROFILE_COMMUNICATION}
 * 8.不需要在每次加入/退出房间时销毁引擎。本示例退出房间时销毁引擎是为了展示完整的使用流程
 *
 * 详细的API文档参见{https://www.volcengine.com/docs/6348/70080}
 */
public class RTCRoomActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private List<String> uids = new ArrayList<>();
    private FrameLayoutAdapter container_adapter;
    private RecyclerView msgrecyclerView;
    private RecyclerView container_recyclerView;
    private MsgAdapter adapter;
    private Button send;
    private EditText inputText;

    private int MESSAGE_ON = 0;
    private int MESSAGE_OFF = 1;
    private int MESSAGE_STATE;

    private ImageView mSpeakerIv;
    private ImageView mAudioIv;
    private ImageView mVideoIv;

    private boolean mIsSpeakerPhone = true;
    private boolean mIsMuteAudio = false;
    private boolean mIsMuteVideo = false;
    private CameraId mCameraID = CameraId.CAMERA_ID_FRONT;

    private FrameLayout mSelfContainer;
    private final FrameLayout[] mRemoteContainerArray = new FrameLayout[3];
    private final TextView[] mUserIdTvArray = new TextView[3];
    private final String[] mShowUidArray = new String[3];

    private RTCVideo mRTCVideo;
    private RTCRoom mRTCRoom;

    private RTCRoomEventHandlerAdapter mIRtcRoomEventHandler = new RTCRoomEventHandlerAdapter() {

        /**
         * 远端主播角色用户加入房间回调。
         */
        @Override
        public void onUserJoined(UserInfo userInfo, int elapsed) {
            super.onUserJoined(userInfo, elapsed);
            Log.d("IRTCRoomEventHandler", "onUserJoined: " + userInfo.getUid());
            uids.add(userInfo.getUid());
        }

        /**
         * 远端用户离开房间回调。
         */
        @Override
        public void onUserLeave(String uid, int reason) {
            super.onUserLeave(uid, reason);
            Log.d("IRTCRoomEventHandler", "onUserLeave: " + uid);
            runOnUiThread(() -> removeRemoteView(uid));
            uids.remove(uid);
        }
    };

    private IRTCVideoEventHandler mIRtcVideoEventHandler = new IRTCVideoEventHandler() {

        /**
         * SDK收到第一帧远端视频解码数据后，用户收到此回调。
         */
        @Override
        public void onFirstRemoteVideoFrameDecoded(RemoteStreamKey remoteStreamKey, VideoFrameInfo frameInfo) {
            super.onFirstRemoteVideoFrameDecoded(remoteStreamKey, frameInfo);
            Log.d("IRTCVideoEventHandler", "onFirstRemoteVideoFrame: " + remoteStreamKey.toString());
            int position = uids.size();
            //runOnUiThread(() -> setRemoteView(remoteStreamKey.getRoomId(), remoteStreamKey.getUserId()));
            runOnUiThread(()->container_adapter.notifyItemChanged(position));
        }

        /**
         * 警告回调，详细可以看 {https://www.volcengine.com/docs/6348/70082#warncode}
         */
        @Override
        public void onWarning(int warn) {
            super.onWarning(warn);
            Log.d("IRTCVideoEventHandler", "onWarning: " + warn);
        }

        /**
         * 错误回调，详细可以看 {https://www.volcengine.com/docs/6348/70082#errorcode}
         */
        @Override
        public void onError(int err) {
            super.onError(err);
            Log.d("IRTCVideoEventHandler", "onError: " + err);
            showAlertDialog(String.format(Locale.US, "error: %d", err));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        String roomId = intent.getStringExtra(Constants.ROOM_ID_EXTRA);
        String userId = intent.getStringExtra(Constants.USER_ID_EXTRA);

        initUI(roomId, userId);
        initEngineAndJoinRoom(roomId, userId);
        container_adapter = new FrameLayoutAdapter(uids, roomId);
        container_recyclerView.setAdapter(container_adapter);
    }

    private void initUI(String roomId, String userId) {
        container_recyclerView = findViewById(R.id.container_recycler_view);
        container_recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        mSelfContainer = findViewById(R.id.self_video_container);
//        mRemoteContainerArray[0] = findViewById(R.id.remote_video_0_container);
//        mRemoteContainerArray[1] = findViewById(R.id.remote_video_1_container);
//        mRemoteContainerArray[2] = findViewById(R.id.remote_video_2_container);
//        mUserIdTvArray[0] = findViewById(R.id.remote_video_0_user_id_tv);
//        mUserIdTvArray[1] = findViewById(R.id.remote_video_1_user_id_tv);
//        mUserIdTvArray[2] = findViewById(R.id.remote_video_2_user_id_tv);
        findViewById(R.id.switch_camera).setOnClickListener((v) -> onSwitchCameraClick());

        mSpeakerIv = findViewById(R.id.switch_audio_router);
        mAudioIv = findViewById(R.id.switch_local_audio);
        mVideoIv = findViewById(R.id.switch_local_video);
        findViewById(R.id.hang_up).setOnClickListener((v) -> onBackPressed());
        MESSAGE_STATE = MESSAGE_OFF;
        findViewById(R.id.switch_message).setOnClickListener((v) -> onMessageClick());
        mSpeakerIv.setOnClickListener((v) -> updateSpeakerStatus());
        mAudioIv.setOnClickListener((v) -> updateLocalAudioStatus());
        mVideoIv.setOnClickListener((v) -> updateLocalVideoStatus());
        //TextView roomIDTV = findViewById(R.id.room_id_text);
        //TextView userIDTV = findViewById(R.id.self_video_user_id_tv);
        //roomIDTV.setText(String.format("RoomID:%s", roomId));
        //userIDTV.setText(String.format("UserID:%s", userId));


        initMsgs();
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        adapter = new MsgAdapter(msgList);
        msgrecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        msgrecyclerView.setLayoutManager(layoutManager);
        msgrecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgrecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }

    private void onMessageClick() {
        FrameLayout message = findViewById(R.id.message_frame);
        if(MESSAGE_STATE == MESSAGE_OFF){
            MESSAGE_STATE = MESSAGE_ON;
            ObjectAnimator ctrlAnimation = ObjectAnimator.ofFloat(message,"translationY",0,-1200);
            ctrlAnimation.setDuration(600);
            ctrlAnimation.setInterpolator(new LinearInterpolator());
            message.bringToFront();
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(message,"alpha",1.0f,0f);
            alphaAnimator.setDuration(1000);
            alphaAnimator.setStartDelay(5000);
            alphaAnimator.setInterpolator(new LinearInterpolator());
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(ctrlAnimation,alphaAnimator);
            set.start();
        }
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy", Msg.TYPE_RECEIVED);
        Msg msg2 = new Msg("Hello guy", Msg.TYPE_SENT);
        Msg msg3 = new Msg("Hello guy", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
    }

    private void initEngineAndJoinRoom(String roomId, String userId) {
        // 创建引擎
        mRTCVideo = RTCVideo.createRTCVideo(getApplicationContext(), Constants.APPID, mIRtcVideoEventHandler, null, null);
        // 设置视频发布参数
        VideoEncoderConfig videoEncoderConfig = new VideoEncoderConfig(360, 640, 15, 800);
        mRTCVideo.setVideoEncoderConfig(videoEncoderConfig);
        setLocalRenderView(userId);
        // 开启本地视频采集
        mRTCVideo.startVideoCapture();
        // 开启本地音频采集
        mRTCVideo.startAudioCapture();
        // 加入房间
        mRTCRoom = mRTCVideo.createRTCRoom(roomId);
        mRTCRoom.setUserVisibility(true);
        mRTCRoom.setRTCRoomEventHandler(mIRtcRoomEventHandler);
        RTCRoomConfig roomConfig = new RTCRoomConfig(ChannelProfile.CHANNEL_PROFILE_COMMUNICATION,
                true, true, true);
        int joinRoomRes = mRTCRoom.joinRoom(Constants.TOKEN,
                UserInfo.create(userId, ""), roomConfig);

        Log.i("TAG", "initEngineAndJoinRoom: " + joinRoomRes);
    }

    private void setLocalRenderView(String uid) {
        VideoCanvas videoCanvas = new VideoCanvas();
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSelfContainer.removeAllViews();
        mSelfContainer.addView(renderView, params);
        videoCanvas.renderView = renderView;
        videoCanvas.uid = uid;
        videoCanvas.isScreen = false;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_HIDDEN;
        // 设置本地视频渲染视图
        mRTCVideo.setLocalVideoCanvas(StreamIndex.STREAM_INDEX_MAIN, videoCanvas);
    }

    public void setRemoteRenderView(String roomId, String uid, FrameLayout container) {
        TextureView renderView = new TextureView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.removeAllViews();
        container.addView(renderView, params);

        Log.d("TAG", "setRemoteRenderView: ");
        VideoCanvas videoCanvas = new VideoCanvas();
        videoCanvas.renderView = renderView;
        videoCanvas.roomId = roomId;
        videoCanvas.uid = uid;
        videoCanvas.isScreen = false;
        videoCanvas.renderMode = VideoCanvas.RENDER_MODE_HIDDEN;
        // 设置远端用户视频渲染视图
        mRTCVideo.setRemoteVideoCanvas(uid, StreamIndex.STREAM_INDEX_MAIN, videoCanvas);
    }

    private void setRemoteView(String roomId, String uid) {
        int emptyInx = -1;
        for (int i = 0; i < mShowUidArray.length; i++) {
            if (TextUtils.isEmpty(mShowUidArray[i]) && emptyInx == -1) {
                emptyInx = i;
            } else if (TextUtils.equals(uid, mShowUidArray[i])) {
                return;
            }
        }
        if (emptyInx < 0) {
            return;
        }
        mShowUidArray[emptyInx] = uid;
        mUserIdTvArray[emptyInx].setText(String.format("UserId:%s", uid));
        setRemoteRenderView(roomId, uid, mRemoteContainerArray[emptyInx]);
    }

    private void removeRemoteView(String uid) {
        for (int i = 0; i < mShowUidArray.length; i++) {
            if (TextUtils.equals(uid, mShowUidArray[i])) {
                mShowUidArray[i] = null;
                mUserIdTvArray[i].setText(null);
                mRemoteContainerArray[i].removeAllViews();
            }
        }
    }

    private void onSwitchCameraClick() {
        // 切换前置/后置摄像头（默认使用前置摄像头）
        if (mCameraID.equals(CameraId.CAMERA_ID_FRONT)) {
            mCameraID = CameraId.CAMERA_ID_BACK;
        } else {
            mCameraID = CameraId.CAMERA_ID_FRONT;
        }
        mRTCVideo.switchCamera(mCameraID);
    }

    private void updateSpeakerStatus() {
        mIsSpeakerPhone = !mIsSpeakerPhone;
        // 设置使用哪种方式播放音频数据
        mRTCVideo.setAudioRoute(mIsSpeakerPhone ? AudioRoute.AUDIO_ROUTE_SPEAKERPHONE
                : AudioRoute.AUDIO_ROUTE_EARPIECE);
        mSpeakerIv.setImageResource(mIsSpeakerPhone ? R.drawable.speaker_on : R.drawable.speaker_off);
    }

    private void updateLocalAudioStatus() {
        mIsMuteAudio = !mIsMuteAudio;
        // 开启/关闭本地音频发送
        if (mIsMuteAudio) {
            mRTCRoom.unpublishStream(MediaStreamType.RTC_MEDIA_STREAM_TYPE_AUDIO);
        } else {
            mRTCRoom.publishStream(MediaStreamType.RTC_MEDIA_STREAM_TYPE_AUDIO);
        }
        mAudioIv.setImageResource(mIsMuteAudio ? R.drawable.mute_audio : R.drawable.normal_audio);
    }

    private void updateLocalVideoStatus() {
        mIsMuteVideo = !mIsMuteVideo;
        if (mIsMuteVideo) {
            // 关闭视频采集
            mRTCVideo.stopVideoCapture();
        } else {
            // 开启视频采集
            mRTCVideo.startVideoCapture();
        }
        mVideoIv.setImageResource(mIsMuteVideo ? R.drawable.mute_video : R.drawable.normal_video);
    }

    private void showAlertDialog(String message) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton("知道了", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    @Override
    public void finish() {
        super.finish();
        // 离开房间
        if (mRTCRoom != null) {
            mRTCRoom.leaveRoom();
            mRTCRoom.destroy();
        }
        mRTCRoom = null;
        // 销毁引擎
        RTCVideo.destroyRTCVideo();
        mIRtcVideoEventHandler = null;
        mIRtcRoomEventHandler = null;
        mRTCVideo = null;
    }
}