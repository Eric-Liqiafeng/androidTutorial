[TOC]

### 官方文档
 - 官方文档: [Android](https://developer.android.google.cn/guide)
### ADB Shell
 - 官方文档：[ADB Shell](https://developer.android.google.cn/studio/command-line/adb?hl=zh_cn#shellcommands)

### Intent

#### 显式调用
#### 隐式调用

### Activity:
#### lifecycle
##### Understand the Activity Lifecycle
Activity 类提供了一系列的callback 方法可以让你知道 activity 的状态改变，比如 创建，停止 或者重新启用activity。
在lifecycle 的回调函数中，你可以声明一些activity的行为，比如当用户正在播放视频的时候，当用户切换到另外一个app 的时候停止视频的播放并且终止network的连接，当用户切回到当前app的时候，重新连接网络，并且当视频回到之前的位置开始播放。
在正确的回调方法里面做正确的事情可以使你的app更加高效和健壮。可以避免以下的问题：
- 当用户接到电话或者切换到另一个app的时候避免崩溃
- 当用户不再使用app的时候不会消耗宝贵的系统资源
- 当用户离开你的app的时候不会丢失用户状态
- 当用户进行横竖屏切换的时候不会崩溃或者丢失用户状态
以下的文档会解析activity的生命周期，首先会介绍下lifecycle的图解，然后回介绍每个回调方法：在执行的时候会发生什么，如何实现回调方法。
最后会介绍几个topic和 activity状态的转变。
lifecycle 最佳实践参考 [Handling Lifecycles with Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle.html) 和 [Saving UI States](https://developer.android.com/topic/libraries/architecture/saving-states.html)
要学习如何使用activity 和一些其他的架构组件构建一个健壮的，可以在生产环境使用的app，请参考[Guide to App Architecture](https://developer.android.com/topic/libraries/architecture/guide.html)

##### Activity-lifecycle concepts
Activity 类提供了六个核心的回调方法：onCreate(), onStart(), onResume(), onPause(), onStop(), and onDestroy(). 当系统进入一个新的状态的时候会回调这些回调方法。
![avatar](./image/activity_lifecycle.png)

![avatar](./image/lifecycle-states.svg)
上图是activity的lifecycle的展示。
当用户离开当前activity的时候，系统会调用方法去拆卸这个activity，在一些case中，这个这种拆卸只会是部分的，这个activity还会保存在内存中，并且还可以把这个activity调回到前台。当用户回到这个activity的时候，这个activity会回到用户离开前的状态。
在一些少数的case中， app在后台的活动是受到严格限制的。[restricted from starting activities when running in the background](https://developer.android.com/guide/components/activities/background-starts)

系统是否会杀死进程取决于activity 的状态[Activity state and ejection from memory](https://developer.android.com/guide/components/activities/activity-lifecycle#asem)

基于你的activity的复杂性，你可能不需要实现所有的回调方法。但是理解这些回调方法是很重要的，并且实现这些方法让app按照用户希望的那样运行。

##### lifecycle callbacks
在一些状况下， 比如 setContentView()属于 activity lifecycle 他们自身，但是实现从属组件动作的代码应该放在组件本身。为了实现这个，你必须使组件具有生命周期意思。详情请看[Handling Lifecycles with Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle.html)去学习如何让从属组件具有生命周期意识。
- onCreate()
你必须实现这个在系统第一次创建activity的时候会调用的回调方法。当activity 创建完成，activity进入Created 状态。在onCreate() 方法，你可以实现一些application 启动逻辑，这个方法只会实现一次。比如你可能会在onCreate() 方法去绑定数据列表， 将activity去关联 一个ViewMode， 和一些实例化的变量。这个方法接受Bundle 类型的参数savedInstanceState,这个参数包含了这个activity之前保存的状态，如果这个activity之前没有保存过，那么这个bundle 的对象的值是null。
如果你有一个有生命周期的从属组件，那么它会收到 ON_CREATE 事件。拥有@OnLifecycleEvent注解的方法会被调用，所以你的又生命周期的从属组件可以在执行一些在created 状态下启动的逻辑。
下面是onCreate() 方法的例子，声明了layout
并且从 参数里面读取之前保存的状态
```java
TextView textView;

// some transient state for the activity instance
String gameState;

@Override
public void onCreate(Bundle savedInstanceState) {
    // call the super class onCreate to complete the creation of activity like
    // the view hierarchy
    super.onCreate(savedInstanceState);

    // recovering the instance state
    if (savedInstanceState != null) {
        gameState = savedInstanceState.getString(GAME_STATE_KEY);
    }

    // set the user interface layout for this activity
    // the layout file is defined in the project res/layout/main_activity.xml file
    setContentView(R.layout.main_activity);

    // initialize member TextView so we can manipulate it later
    textView = (TextView) findViewById(R.id.text_view);
}

// This callback is called only when there is a saved instance that is previously saved by using
// onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
// other state here, possibly usable after onStart() has completed.
// The savedInstanceState Bundle is same as the one used in onCreate().
@Override
public void onRestoreInstanceState(Bundle savedInstanceState) {
    textView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
}

// invoked when the activity may be temporarily destroyed, save the instance state here
@Override
public void onSaveInstanceState(Bundle outState) {
    outState.putString(GAME_STATE_KEY, gameState);
    outState.putString(TEXT_VIEW_KEY, textView.getText());

    // call superclass to save any view hierarchy
    super.onSaveInstanceState(outState);
}
```
作为定义在XML layout文件中，然后使用setContentView()来定义视图的一种替代方案，你可以创建一个新的View 对象，然后通过代码往ViewGroup 中插入View 来构建视图层次。更多的关于创建用户界面的信息，请参考[User Interface](https://developer.android.com/guide/topics/ui/index.html) 文档。
activity不会驻留在Created 状态，当onCreate() 方法调用完成之后，activity进入 Started 状态。系统会快速连续调用onStart()和onResume() 方法。

- onStart() 
当activity进入Started 状态时，系统调用onStart() 回调方法。这个方法使得用户可以看到activity，并且准备进入 前台(foreground) 和用户交互。这个方法是app初始化UI code 的方法。
当activity 进入Started 状态时，有生命周期的从属组件会收到ON_START 事件。
onStart() 方法会很快完成，并且和Created 状态一样，activity也不会驻留在Started 状态，当onStart() 回调完成时，activity会进入 Resumed 状态，并且调用 onResume() 方法。

- onResume()
当activity 进入Resumed 状态时，activity就到了前台(foreground)，然后系统会调用 onResume() 回调方法。
这是app与用户交互的状态。app会保持这个状态直到用户不再focus 这个app。比如有电话进来，用户跳转到另一个app，或者设备的屏幕关闭。
当activity进入resume 状态，它的有生命周期的从属组件会收到ON_RESUME事件，这里是从属组件可以开始它的功能当组件已经可见并且已经在前台(foreground)， 比如开始摄像头预览。
当中断事件发生的时候，activity 进入Paused 状态，系统会调用onPause() 回调方法。
当activity从Paused 状态回到 Resumed时，系统会调用onResume() 方法，所以你必须重新初始化那些在onPause() 方法中释放的组件，和执行其他必须在activity进入Paused状态时的初始化。
下面是lifecycle-aware组件：当收到ON_RESUME事件中访问摄像头
```java
public class CameraComponent implements LifecycleObserver {

    ...

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initializeCamera() {
        if (camera == null) {
            getCamera();
        }
    }

    ...
}
```
上面的code 是关于[LifecycleObserver](https://developer.android.com/reference/androidx/lifecycle/LifecycleObserver)接收到 ON_RESUME事件时初始化相机。在多窗口模式你的activity 可能处于完全可见但是是Paused 状态。比如用户处于多窗口模式，但是没有触及你的activity，你的activity会处于Paused 状态，如果你想让你的相机只有在app处于Resumed(可见并处于前台 visible and foreground) 状态时active，那就在ON_RESUME 事件中完成初始化。如果你想让你的相机在activity处于Paused 但是可见 (visible)的状态中仍然active，你应该在ON_START事件中初始化相机。然而，当你的activity 处于Paused状态时的相机访问 有可能因为有处于Resumed状态的activity也在使用相机的情况下被拒绝。
有时，你有可能需要在activity处于Paused的时候保持相机处于active状态，但是这个有可能降低用户的体验。在多窗口模式下，你需要更小心的控制生命周期和共享系统资源.学习更多的多窗口模式，请看[Multi-Window Support](https://developer.android.com/guide/topics/ui/multi-window.html)
不管你选择了哪一个事件去初始化组件，请确保选择合适的lifecycle 去释放资源。
如果你用ON_START 事件初始化，用ON_STOP事件释放，如果你用ON_RESUME事件初始化，用ON_PAUSE释放。这些代码可以写在lifecycle-aware 组件上，你也可以直接写在 activity的回调方法中，但是如果你使用了lifecycle-aware组件，那么这个组件可以在多个activity 中复用。详情请参考[Handling Lifecycles with Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle.html) 如何创建一个lifecycle-aware组件。

- onPause()
当用户离开你的activity会调用这个方法(这个并不意味着这个activity会被destory)。 这表明activity不再在前台(尽管在多窗口模式下，该activity仍然是可见的)。使用onPause()方法去停止或者调整操作不应该花费太长的时间，当activity 在Paused状态时，你希望它能快速恢复。以下是activity可能进入Paused状态的一些例子：
  - 一些中断事件的执行，这种是最常见的情况。
  - 在android 7.0(API level 24) 或者更高，多个app在多个窗口运行，因为用户同一时间只会操作一个app，系统会暂停其他所有app。
  - 输入框打开的时候，activity仍可见，但是是Paused 状态。
当activity转成 Paused 状态， 所有绑定在该activity的lifecycle-aware组件都会收到 ON_PAUSE事件。lifecycle-aware组件可以停止所有function的运行 当组件已经不在前台， 比如相机预览。
你也可以在onPause() 方法中释放系统资源。处理传感器(比如GPS)或者其他用户在Paused状态下不需要的系统资源。像之前说的，处在Paused状态下的activity有可能还是可见的，所以应该考虑在onStop() 方法中释放所有的资源而不是在onPause() 中。
下面是lifecycle-aware组件：当收到ON_PAUSE事件中释放摄像头的例子
```java
public class JavaCameraComponent implements LifecycleObserver {

    ...

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    ...
}
```
onPause() 操作时非常简短的，不一定有足够的事件来执行保存操作。基于这个原因，你不应该使用onPause() 方法来保存用户数据，进行network 访问，或者执行数据库事务。这些工作可能不能在方法执行完之前完成。所以，你应该在onStop() 方法中执行那些繁琐的shutdown 操作。关于更多的保存数据操作，请看 [Saving and restoring activity state](https://developer.android.com/guide/components/activities/activity-lifecycle#saras)
执行完onPause() 方法并不意味着用户activity会离开Paused 状态，activity会保持Paused状态直到activity被重新唤醒的时候执行 onResume() 回调。当activity的状态从Paused 状态转变成Resumed状态的时候，系统会保存这个activity的实例在内存中，然后调用 onResume() 方法。在这种场景中，你不需要在任何回调方法来恢复之前的状态。当activity变得完全不可见的时候，系统会调用 onStop() 方法。

- onStop()
当用户对用户不可见的时候，activity会进入Stopped 状态。系统会调用onStop() 回调方法。举个例子，当有新的activity覆盖整个屏幕的时候。当activity运行结束之后，系统会调用onStop() 方法，这个是 to be terminated。
当activity转到Stopped 状态，所有绑定的lifecycle-aware 组件会收到 ON_STOP事件，这个是lifecycle 组件可以停止所有不需要再界面不可见时运行的function。
在onStop() 方法，app应该释放或者调整那些在app 不可见时不需要的资源。比如，你的app可能会停止动画，或者把位置更新从细粒度转到粗粒度。
你通常需要使用onStop() 方法来关闭那些CPU-intensive的操作。
例如，如果你找不到一个合适的时机来保存信息到数据库，你可以在onStop()方法中进行这个操作。以下时在onStop() 方法中保存数据库的例子。
```java
@Override
protected void onStop() {
    // call the superclass method first
    super.onStop();

    // save the note's current draft, because the activity is stopping
    // and we want to be sure the current note progress isn't lost.
    ContentValues values = new ContentValues();
    values.put(NotePad.Notes.COLUMN_NAME_NOTE, getCurrentNoteText());
    values.put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle());

    // do this update in background on an AsyncQueryHandler or equivalent
    asyncQueryHandler.startUpdate (
            mToken,  // int token to correlate calls
            null,    // cookie, not used here
            uri,    // The URI for the note to update.
            values,  // The map of column names and new values to apply to them.
            null,    // No SELECT criteria are used.
            null     // No WHERE columns are used.
    );
}
```
这个例子是直接使用 SQLite，你可能应该使用Room，一个基于SQLite 的抽象存储库。了解更多请看[Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room.html)
当activity 进入Stopped 状态，activity 对象还会保留在内存中，它维护所有状态和成员信息，但并不是attach在窗口管理器。当 activity 恢复的时候，activity 重新调用这些信息，你不需要在任何初始化组件中创建任何回调方法来恢复之前的Resumed状态。系统也会跟踪在layout中所有view的当前状态。所以，当用户在EditText中输入信息的时候，信息会保留而不需要你手动保存。
<table><tr><td bgcolor=#e1f5fe>
<font color="#01579b" size=4>
Note:
当你的activity 处于Stopped 状态时，系统可能为了恢复内存而去销毁进程，就算系统销毁了处于Stopped 状态的进程，系统还是会保留这些View Object 中的状态 到 一个 Bundle 对象，然后在用户返回这个activity的时候还原。如果要看更多的还原activity的操作，请看[ Saving and restoring activity state](https://developer.android.com/guide/components/activities/activity-lifecycle#saras)
</font>
</td></tr></table>
从Stopped状态，activity要么回到跟用户交互，要么在activity完成之后消失。如果activity回到与用户交互，系统会调用 onRestart() 方法，如果activity 运行结束，系统会调用onDestory() 方法。

- onDestory()
 onDestory() 方法会在activity被destory之前调用。系统会调用这两个方法基于下面两种情况：
   - activity完成(基于用户解散这个activity或者finish() 方法被调用)
   - 系统由于 configuration change(例如设备旋转或者多窗口模式))临时销毁activity 
当activity 变成 Destoryed状态， 所有lifecycle-aware 组件会受到 ON_DESTROY 事件，lifecycle 组件会在activity 被销毁之前清除所有的东西。
应该使用ViewModel 对象去保存view data 而不是在里面写决定为什么销毁activity的逻辑。如果activity 由于configuration change 要重新创建activity，ViewModel 不需要做任何事，因为它将被保留下来给下一个activity实例。如果activity不准备重新创建，那么ViewModel 将会调用onCleared() 方法，会清除所有数据。
你可以根据isFinishing()区分下面两种场景。
如果activity finish，onDestory() 方法是生命周期最后一个回调函数。如果onDestory() 是configuration change的结果，系统会立即创建一个新的activity实例，然后用新的configuration调用onCreate() 方法。
onDestory() 回调应该释放所有的在onStop()中没有释放的资源。

##### Activity state and ejection from memory
系统会kill进程当他需要释放内存的时候，系统kill进程的可能性有可能取决于进程当时的状态。反过来，进程的状态取决于进程中activity的运行状态。下图表示了进程状态，activity 状态和被系统kill的可能性
![avatar](./image/process_activity_system_kill.png)
系统用户不会直接kill 一个 activity来释放内存。系统会kill 进程，包括activity和其他运行在其中的所有东西。学习如何保存和恢复activity UI 状态当系统kill 进程的时候。请看 [Saving and restoring activity state](https://developer.android.com/guide/components/activities/activity-lifecycle#saras)















#### state change
#### Task and Back Stack
#### process and application lifecycle
- JobServiceW
##### Parcelable and Boundle


### Service
#### overview
##### three different type
- Foreground
必须显示一个通知， foreground service 会一直运行，即使用户没有跟这个 service 所属的app进行交互。
- Background
后台运行的service 不会直接通知用户 Android API level 26 以上当你的app 不在前台时 会增加 后台service的限制，详情请看 [restrictions on running background services](https://developer.android.google.cn/about/versions/oreo/background.html)
-Bound
bound service 提供一个client-server 接口用来作为组件和service之间的通信。

##### Service and Thread
- [Processes and Threading](https://developer.android.google.cn/guide/components/processes-and-threads.html#Threads)
##### The basics

如果组件调用 startService() service不会停止，除非 service 自己调用 stopSelf() 或者其他组件调用了stopService()

如果组件调用 bindService() 方法去创建service 而没有调用onStartCommand 方法，service 只会在组件绑定的时间内运行，一旦所有组件都跟这个service 解绑，那么这个服务会被销毁

当系统内存不足时，系统会去销毁service，
当一个service 被组件绑定且用户focus时，这个service 不太可能会被销毁

当一个service 定义了在后台运行时，it is rarely killed.

如果你的服务长时间运行了很久，你的service 变得容易被系统杀死，如果你的系统被杀死了，你需要优雅的处理重启，
如果你想了解更多的 系统什么时候会杀死你的service 请看
[Processes and Threading](https://developer.android.google.cn/guide/components/processes-and-threads.html#Threads)

##### Create a started service
 组件调用 startService() 方法最终会调用 onStart Command() 方法。当service 启动了，它的生命周期与启动他的组件没有依赖关系，哪怕 启动它的组件被销毁了。

 service 可以通过stopSelf() 或者由其他组件调用 stopService() 来停止服务。

 - Service 
 这个是所有Service 的基类，是绑定在 Application MainThread 中运行的，会拖慢所有正在运行的activity 的性能。建议在service中另开一个线程去执行你的job。
 onStartCommand() 方法返回值必须是Integer 类型的，这个返回值描述了当系统杀死了该服务应当如何重新启动。
 返回值必须是以下常量：
    - *START_NOT_STICKY*: 值为2，表示当没有pending的Intent 时系统不会重新create 服务。
    - *START_STICKY*: 值为1，系统重新创建service，并且调用 onStartCommand() 方法，但是不会重新传递上一次的intent。适用于音频播放场景
    - *START_REDELIVER_INTENT*： 值为3，系统重新创建service，并且调用onStartCommand() 方法，并且重新传递上一次的intent.所有pending的intent 会轮流传过来。适用于下载场景。

- IntentService
 这个是Service 的子类，用了worker线程去处理所有请求，如果你不需要同时处理多个请求，这个是最好的选择。
实现 onHandleIntent() 这个方法接受每个 request 传过来的Intent。  
如果你要实现一个IntentService的子类，那么除了 onHandleIntent() 方法和onBind() 方法，其他的方法，包括构造方法都要调用父类的相对应的方法。

- Start a service
你可以在Activity 或者其他 application 组件中通过调用 startService() 或者 startForegroundService() 方法启动服务。Android系统会调用 onStartCommand() 方法传递Intent。
```java
   Intent intent = new Intent(this, HelloService.class);
   startService(intent);
```

- Stop Service
一个已经启动的service 必须管理自己的生命周期。这是因为系统并不会stop 或者destory service 除非系统内存不足，但是之后系统还是会根据 onStartCommand() 的返回值重启service。service 可以通过自身调用 stopSelf()方法或者其他的组件调用 stopService() 方法去停止service。
一旦调用了stopSelf() 或者 stopService() 方法，系统会尽可能快销毁service。
如果你的service 在onStartCommand处理多个request，那么你不应该在service 接受新的request中停止你的service，因为这有可能导致新的request中止执行。为了避免这个问题，应该使用 stopSelf(int) 方法去确保在stop service 时是基于最新的start request。当你调用stopSelf(int) 方法时，传递进来的是startId, 那么当有新的request 进来时， startId 会不匹配，那么service 将不会停止。
<font color="#bf360c" size=4>为了避免系统资源浪费，你应该在service做完工作之后就把它停止，或者在其他组件中调用stopService()将它停止。</font>

##### Create a bound service
一个bound service 允许组件通过调用bindService() 方法来创建一个长期连接。通常不会允许组件通过调用startService() 方法去启动一个service。
bound service 只会存活于它与其它组件的绑定期，当没有组件跟它绑定的时候，该service就会被系统destory.创建绑定服务要实现onBind() 方法并且返回一个 IBinder,一个service 可以绑定多个组件，称为client,即客户端，客户端可以根据这个IBinder 在客户端和service进行通讯。
多个客户端可以同时绑定同一个服务。当一个client 要停止跟service工作，可以调用unBindService() 方法取消绑定。当没有client 绑定service， 系统会destroy service。
bound service 比start service 要复杂得多，详细要看[bound services](https://developer.android.google.cn/guide/components/bound-services.html)

##### Send notification to the user
当服务正在运行，当有event 发生时，可以通过 [Toast Notifications](https://developer.android.google.cn/guide/topics/ui/notifiers/toasts.html) 或者[Status Bar Notifications](https://developer.android.google.cn/guide/topics/ui/notifiers/notifications.html) 来通知用户。

##### Running a service in the foreground
foreground service 不会在系统低内存时要杀service的候选名单上。一个foreground service 必须提供一个status bar 的状态栏。这个通知是一直在运行的，意味着在service stop或者从foreground移除前不会消失。
<table><tr><td bgcolor=#feefe3>
<font color="#bf360c" size=4>
限制app 使用foreground service
让用户知道你的service在干什么</font>
</td></tr></table>
例子： 音乐播放服务应该作为一个foreground service。
<table><tr><td bgcolor=#e1f5fe>
<font color="#01579b" size=4>
Note:
Android 9 (API level 28) 或者以上版本如果要使用foreground service 必须申请FOREGROUND_SERVICE 权限，如果没有申请该权限，会抛出 SecurityException 异常。
</font>
</td></tr></table>
要将service 启动为 foreground service， 调用startForeground 方法，传入两个参数， 一个不为0 的唯一标识nitification身份的 Integer ，一个notification。

```java
Intent notificationIntent = new Intent(this, ExampleActivity.class);
PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, notificationIntent, 0);

Notification notification =
          new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
    .setContentTitle(getText(R.string.notification_title))
    .setContentText(getText(R.string.notification_message))
    .setSmallIcon(R.drawable.icon)
    .setContentIntent(pendingIntent)
    .setTicker(getText(R.string.ticker_text))
    .build();

startForeground(ONGOING_NOTIFICATION_ID, notification);
```

想了解更多关于 notification 的信息，请参考 [Create Status Bar Notification](https://developer.android.google.cn/guide/topics/ui/notifiers/notifications.html)

##### Managing the life cycle of a service
跟activity 一样， service 也有它的lifecycle 回调方法。
```java
public class ExampleService extends Service {
    int startMode;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return mStartMode;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }
}
```
<table><tr><td bgcolor=#e1f5fe>
<font color="#01579b" size=4>
Note:
跟Activity lifecycle回调方法不同， service lifecycle 回调方法不需要调用 父类的实现方法。
</font>
</td></tr></table>

![avatar](./image/service_lifecycle.png)

entire lifetime 在 onCreate() 方法和onDestory() 之间，
active lifetime，start service active 生命周期跟entire lifetime 一致， bound service active lifetime 在 onUnbind() 回调方法之后结束。
#### Create a background service


























