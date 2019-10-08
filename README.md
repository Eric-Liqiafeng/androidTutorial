[TOC]

### 官方文档
 - 官方文档: [Android](https://developer.android.google.cn/guide)
### ADB Shell
 - 官方文档：[ADB Shell](https://developer.android.google.cn/studio/command-line/adb?hl=zh_cn#shellcommands)

### Intent and Intent Filters
#### Overview
Intent是

#### 隐式调用

### Activity:
#### Understand the Activity Lifecycle
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
用户也可以通过应用管理器去杀死进程。

想了解更多的进程相关的，请参考[Processes and Threads](https://developer.android.google.cn/guide/components/processes-and-threads.html)。想了解更多的进程的生命周期和activity绑定的信息，请参考[Process Lifecycle](https://developer.android.google.cn/guide/components/processes-and-threads.html#Lifecycle)。

##### Saving and restoring transient UI state
一个用户希望当configuration change 像屏幕旋转或者进入多窗口模式的时候
activity的UI跟之前保持一致。然而，当这些configuration change 发生的时候，系统默认销毁activity， 包括存储在activity里面的UI状态。同样的，用户也希望当他切换到别的app再回来的时候，也希望activity的UI状态保持不变。然而，当你的activity处于Stopped 状态时，系统可能会kill了你的进程。
当你的activity因为系统需求约束销毁的时候，你应该使用ViewModel，onSaveInstanceState() 或者local storage 去保存UI状态。想学习更多关于用户期望和系统行为和如何在系统初始化activity和进程死亡之间最好的保存复杂的UI状态，请看[Saving UI State](https://developer.android.google.cn/topic/libraries/architecture/saving-states.html)。
这一小节简单介绍了什么是instance state和如何使用onSaveInstance()方法，如果你的UI 数据时简单和轻量级的，比如一些基础类型和简单对象比如String，你可以只使用onSaveInstanceState()去保存UI状态。在其他大部分情况，你都应该使用ViewModel和onSaveInstanceState()，当onSaveInstanceState()可能会导致序列化和反序列化的性能消耗时。

##### Instance state
有几个场景activity被destory 时因为app的正常行为，比如用户按下返回按钮或者你的activity通过调用finfish() 方法。当因为用户按下返回按钮或者activity自己finish，无论是系统还是用户意识中，这个activity 实例已经永远销毁了。在这些场景中，用户希望的和系统行为一致，所以没有什么额外的工作要做。
然而，当系统因为系统约束，比如configuration change或者内存不足而destroy activity时，虽然实际上 activity实例已经销毁了，但是系统会记住它是存在的，如果用户试图导航回到activity，系统使用一组描述activity销毁状态前的数据创建一个新的activity 实例。
系统使用的恢复之前状态的保存的数据称为实例状态(Instance state),这是一个键值对存储在一个Bundle对象的集合。默认情况下，系统使用Bundle 实例去保存每个在activity布局中的View 对象(比如EditText widget 中的text value)的信息。所以，当你的activity实例被destroy和重新创建，这些layout的状态会还原到以前的状态，而不需要你手写代码。然而，你的activity可能有更多的状态信息要存储，比如一些用来追踪用户activity进展的成员变量。
- Note:
为了让Android系统恢复活动的View object的状态，每个视图都必须有一个唯一的id提供给 android:id 属性。
这个Bundle对象不适合用来保存很多琐碎的数据量，因为他要在主线程上进行序列化并且消耗进程的内存。为了在Bundle保存少量的数据，你应该该结合本地缓存和local storage来保存数据。即结合 onSavwInstanceState() 方法，ViewModel, 详情请看 [Saving UI States](https://developer.android.google.cn/topic/libraries/architecture/saving-states.html)。
##### Saving simple ,ligheweight UI state using onSaveInstanceState()
当你的activity开始stop的时候，系统会调用 onSaveInstanceState() 方法，这样你的activity可以将状态信息保存到一个Bundle 对象。该方法的默认实现保存一些activity的视图层次，比如一些小部件EditText widget 的text信息或者ListView widget 的滚动的位置。
为了给activity保存额外的的实例状态(Instance state)的信息,你必须重写 onSaveInstanceState() 方法，然后加一些键值对到Bundle 对象上，当你的activity被意外销毁时。当你重写onSaveInstanceState()方法时，如果你想保存视图层级的状态，你必须调用父类的onSaveInstanceState() 方法。
```java
static final String STATE_SCORE = "playerScore";
static final String STATE_LEVEL = "playerLevel";
// ...


@Override
public void onSaveInstanceState(Bundle savedInstanceState) {
    // Save the user's current game state
    savedInstanceState.putInt(STATE_SCORE, currentScore);
    savedInstanceState.putInt(STATE_LEVEL, currentLevel);

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
}
```
- Note:
当用户显式地关闭activity时或者activity 的finfish() 方法被调用时onSaveInstanceState() 方法不会被调用。
为了保存一些持久化数据，比如user preferences 或者保存一些数据到数据库，你应该在activity在前台的时候找个合适的时机。如果你找不到合适时机，你应该在onStop() 方法中做这些事情。
##### Restore activity UI state using saved instance state
当你的activity是在被destroy后重新创建，你可以通过系统传递过来的Bundle对象恢复之前的实例状态(Instance state)。onCreate() 和onRestoreInstanceState() 回调方法都会使用一样的Bundle 对象。
因为onCreate() 方法是在activity方法被创建的时候都会被调用的，(可能存在第一次创建的时候，Bundle 对象为null)，所以必须在使用前判断Bundle 对象是否为null。如果Bundle 对象是null,那么系统是在创建一个新的activity实例而不是恢复之前被销毁的实例。
下面是你可以在onCreate() 方法中恢复实例状态的例子：
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); // Always call the superclass first

    // Check whether we're recreating a previously destroyed instance
    if (savedInstanceState != null) {
        // Restore value of members from saved state
        currentScore = savedInstanceState.getInt(STATE_SCORE);
        currentLevel = savedInstanceState.getInt(STATE_LEVEL);
    } else {
        // Probably initialize members with default values for a new instance
    }
    // ...
}
```
你也可以使用onRestoreInstancState() 方法，这个方法会在onStart() 方法被调用之后调用。系统只有在存在保存实例状态的时候(即 Bundle 对象不为null)调用，所以不需要检查Bundle 对象是否为null。
```java
public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Always call the superclass so it can restore the view hierarchy
    super.onRestoreInstanceState(savedInstanceState);

    // Restore state members from saved instance
    currentScore = savedInstanceState.getInt(STATE_SCORE);
    currentLevel = savedInstanceState.getInt(STATE_LEVEL);
}
```
- Caution:
调用父类的onRestoreInstanceState() 方法默认实现可以恢复视图层面的状态。
#### Navigating between activities
一个app在生命周期中可能进入和退出一个activity。比如用户可以使用设备的返回按键或者activity需要启动一个不同的activity。本节介绍activity之间的跳转。这些主题包括从一个activity启动另一个activity，保存activity的状态和恢复activity的状态。

##### Starting one activity from another
一个activity通常需要启动另一个activity，如果当app需要从当前屏幕移动到一个新的activity。
取决于你的activity想不想新的activity有返回结果，你的启动activity的方法可以非为两个 startActivity()或者startActivityForResult()。在这两种情况下，你传递的都是Intent 对象。
Intent 对象描述了你想启动的activity或者描述了你想启动activity的类型(系统选择当前app或者甚至是不同app中的适当的应用)。Intent对象也可以传递一些少量的数据用来给activity启动使用，想了解更多的关于Intent 类，请参考[ Intents and Intent Filters](https://developer.android.google.cn/guide/components/intents-filters.html)。
 
 ###### startActivity()
如果你启动一个新的activity不需要返回结果，你可以调用startActivity()方法。
当你在自己的app内，你经常需要简单地启动你知道的activity。例如下面代码显示了如何启动一个称为 SignActivity的activity。
```java
Intent intent = new Intent(this, SignInActivity.class);
startActivity(intent);
```
您的应用程序可能也要从您的活动执行一些操作,如发送电子邮件,短信,或状态更新,使用数据。在这种情况下,您的应用程序可能没有自己的活动来执行这样的行为,所以你可以利用活动提供的其他应用程序在设备上,它可以执行的操作。这是意图很有价值的地方:您可以创建一个意图描述你要执行的操作和系统启动适当的活动从另一个应用程序。如果有多个活动,可以处理的目的,然后,用户可以选择使用哪一个。例如,如果您想允许用户发送一封电子邮件,您可以创建以下Intent:
```java
Intent intent = new Intent(Intent.ACTION_SEND);
intent.putExtra(Intent.EXTRA_EMAIL, recipientArray);
startActivity(intent);
```
EXTRA_EMAIL额外添加到意图是一个字符串数组的电子邮件应该发送电子邮件地址。当一个电子邮件应用程序响应这个意图,它读取字符串数组中提供额外的和地方的”到“电子邮件领域组成形式。在这种情况下,电子邮件应用程序的活动开始,当用户完成时,resumes你的activity。
###### startActivityForResult()
有些时候，你需要从一个activity中得到返回结果。
例如,你可能会开始一个activity,让用户在一个联系人列表中选择一个人;当它结束时,它将返回被选中的人。要做到这一点,您调用startActivityForResult(Intent,int)方法,在整数参数标识返回结果的回调，用来区分一个activity中多个startActivityForResult(Intent, int)的响应逻辑。它不是全局标识符，不会与其他的app或者activity起冲突。返回来的结果的回调方法是onActivityResult(int,int,Intent)。
当一个子activity退出时，它可以调用setResult(int)去返回数据给父activity。子活动活动必须提供一个结果代码,可以是标准的返回结果RESULT_CANCELED,RESULT_OK，或者是从RESULT_FIRST_USER 开始的任何自定义值。此外，子activity可以包含任何数据的Intent 对象。父activity使用 onActivityResult(int,int,Intent)方法通过父活动提供的整数标识符来接收信息。
如果子activity因为任何原因失败了，比如说崩溃了，父activity会接收到代码 RESULT_CANCELED。
```java
public class MyActivity extends Activity {
     // ...

     static final int PICK_CONTACT_REQUEST = 0;

     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
             // When the user center presses, let them pick a contact.
             startActivityForResult(
                 new Intent(Intent.ACTION_PICK,
                 new Uri("content://contacts")),
                 PICK_CONTACT_REQUEST);
            return true;
         }
         return false;
     }

     protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == PICK_CONTACT_REQUEST) {
             if (resultCode == RESULT_OK) {
                 // A contact was picked.  Here we will just display it
                 // to the user.
                 startActivity(new Intent(Intent.ACTION_VIEW, data));
             }
         }
     }
 }
```
###### Coordinating activities
当一个activity启动另一个activity的时候，他们都经历activity的转换。当另一个activity创建的时候，第一个activity进入Paused 或者 Stopped 状态。当这些activity保存共享数据到磁盘或者其他地方的时候，理解下面的很重要，并不是第一个activity完全stopped之后才创建第二个activity。相反，是在启动第二个activity的过程中停止第一个activity。
lifecycle 回调的顺序需要好好的定义，尤其是当两个activities在同一个app内，并且由由一个启动另外一个的时候。下面是activity A启动activity B 时的操作顺序：
  - 1.activity A 的onPause() 方法执行。
  - 2.activity B 的onCreate()，onStart() 和onResume() 方法执行，现在 activity B 是用户 focus的了。
  - 3. 然后 activity A 已经在屏幕上不可见了，它的onStop() 方法执行。
  这个预测的生命周期的顺序允许你管理从一个activity到另一个activity的信息转换。

### Activity state change
不同的事件，有可能是用户触发的，有可能是系统触发的，都会导致Activity的状态转换。本文档描述了一些常见的状态转换的情况以及处理这种状态转换的方法。想了解更多的关于activity的状态，请参考[Understand the Activity Lifecycle](https://developer.android.google.cn/guide/components/activities/activity-lifecycle.html)。想学习ViewModel类如何帮助你管理activity的生命周期，请参考[Understand the ViewModel Class](https://developer.android.google.cn/topic/libraries/architecture/viewmodel.html)。
#### Configuration change occurs
有几个事件可以触发configuration change，最常见的例子可能是设备水平和竖直的变化。其他的包括语言的改变或者输入法的改变都会导致configuration change.
当configuration change发生的时候，activity会被销毁和重新创建。原来的activity实例可能会触发 onPause(),onStop(),和onDestroy()回调方法。一个新的activity实例会被创建，然后调用 onCreate(),onStart(),和onResume() 回调方法。
当configuration change时，混合使用ViewModel,onSaveInstanceState()和本地存储来保存activity的UI状态。如何结合这些方法取决于你的UI data的复杂程度，需要考虑检索速度和内存占用。想了解更多的保存activity的UI状态，请看 [Saving UI States](https://developer.android.google.cn/topic/libraries/architecture/saving-states.html)。
##### Handling multi-window cases
当一个app进入多窗口模式时，在Android 7.0(API level 24)或者更高版本可用，系统会通知当前正在运行的activity的configuration，activity的生命周期会发生变化。在处于多窗口模式下的app而言，resize窗口大小也会触发上述行为。你的activity可以自己处理这些configuration change，或者允许系统销毁你的activity然后用新的config去重新创建activity。
想了解更多关于多窗口的生命周期，请看[ Multi-Window Support ](https://developer.android.google.cn/guide/topics/ui/multi-window.html)的[ Multi-Window Lifecycle](https://developer.android.google.cn/guide/topics/ui/multi-window.html#lifecycle)小节。
在多窗口模式下，虽然有两个app对用户来说是可见的，但是只有一个app是在前台与用户交互的，这个app处于Resumed状态，而另一个app处于Paused状态。
当用户从app A 转到 app B,系统调用app A 的onPause() 方法，调用app B 的onResume() 方法。
想了解更多关于多窗口模式的细节，请看[Muti-Window Support](https://developer.android.google.cn/guide/topics/ui/multi-window.html)。
##### Activity or dialog appears in foreground
当一个新的activity或者弹框出现在前台，taking focus 部分覆盖正在运行的activity，被覆盖的activity失去焦点进入Paused 状态，系统调用onPause() 方法。
当被覆盖的activity重新回到前台重新获得焦点，就会调用onResume() 方法。
当一个新的activity或者弹框出现在前台，并且完全覆盖正在运行的activity，这个activity失去焦点并且进入Stopped 状态，系统会快速调用onPause() 和 onStop() 方法。
当这个被覆盖的activity 实例重新回到前台的时候，系统调用onRestart() onStart() 和onResume() 方法，当一个新的实例覆盖activity的时候，系统不会调用onRestart() 方法，只会调用onStart()和onResume().
- note:
当用户按下home按钮时，当前activity的系统行为表现为完全覆盖。
### Test your activities
详见[Test your app's activities](https://developer.android.google.cn/guide/components/activities/testing)
### Task and Back Stack
#### Understand Tasks and Back Stack
当用户在执行一项确定的工作时，用户与之交互的activity的集合称为一个task。这些activity根据打开的顺序放在back stack里面。例如一个电子邮件app可能有一个activity来显示新消息的列表。当用户选择一个message时，会打开一个新的activity来查看这条message。这个新的activity会被加到back stack。当用户按下返回按钮，这个新的activity会finished然后从stack里面pop出来。
当app同时在多窗口模式(在android 7 API level 24 以上支持)下运行，系统会为每个窗口分别管理任务。每个窗口可能有多个任务。这个同样适用于[ Android apps running on Chromebooks](https://developer.android.google.cn/topic/arc/index.html)，系统在每个窗口上管理task，或者一组task。
设备主屏幕是大多数task的起点。当用户点击app的launcher icon时，app的task变成前台(foreground)。如果这个app没有task存在(最近没有使用),那么一个新的task就会被创建，app的main activity就会打开，作为栈的根。
当当前的activity启动另一个activity，这个新的activity会被push到stack的顶部，获得焦点，之前的activity会保留在stack里面，但是状态是Stopped的。当activity stop，修通会保留用户界面的状态。当用户点击返回按钮，当前的activity会从stack的顶部pop出来(这个activity会被destroy),之前的activity resumes.在stack中的activities永远不会rearranged，只有从stack中push进去或者pop出来，当从当前activity中启动时，push到stack中，当用户按下返回按钮时，从stack中pop出来。因此，back stack 是个后出先进的stack。下图显示了这一行为：
![avatar](./image/diagram_backstack.png)
如果用户继续点击返回，那么在stack中的每个activity都会被pop出来，直到返回主界面(or to whichever activity was running when the task began)。当所有的activities都从stack移除的时候，这个task也就不存在了。
一个task是一个凝聚单元，当用户开始一个新的task或者通过Home 按钮跳转到主界面的时候，可以移动到 'background'。
当task处于background时，所有在这个task中的activities 都是stopped状态的，但是task是完好无损的，这个task只是简单地失去了焦点，如下图所示：
![avatar](./image/diagram_multitasking.png)
一个任务可以返回前台，用户可以从他之前离开的地方重新开始。举个例子，一个task A 有三个activities，用户按下home 按钮，然后启动一个新的app。当回到主界面的时候，这个task就在background了。当新的app B启动的时候，系统会为新的app B启动一个新的task。当用户返回主界面并且选择之前启动过的app A时， task A就到了foreground，它的三个activity都是没有任何改变的，然后处于stack顶部的activity会resumes。在这个时候，用户也可以通过主界面或者使用[Recents screen](https://developer.android.google.cn/guide/components/recents.html)切换回之前的task B，这个就是android的多任务的例子。
- note:
多任务可以在后台运行。但是当用户运行了太多的background task的时候，系统可能会destroy一些background activities来释放内存，可能会导致activity的状态丢失。
因为在back stack 中的activities永远不会rearranged，如果你的app允许用户通过不止一个activity启动一个activity，一个新的activity实例会被创建并且push到stack里面而不是把之前的activity实例置顶。因此，在你的应用程序中，一个activity可能会被实例化多次，如下图所示：
![avatar](./image/diagram_multiple_instances.png)
同样的，当用户通过返回按钮返回，每个在task中的activity都会顺序打开(每个都有它们自己的UI state)。然而如果你可以改变这种行为，比如让activity只实例化一次。这部分我们会在下面的小节[Managing Tasks](https://developer.android.google.cn/guide/components/activities/tasks-and-back-stack#ManagingTasks)中会介绍。
总结 activities和task的默认行为
- 当activity A 启动activity B,activity A 被stop，但是系统保留其状态比如滚动的位置和文本的输入。当用户按下返回键的时候，activity A resumes,它的状态会被还原。
- 当用户按下Home键离开这个task的时候，当前的activity是stopped的，它的task变成background的。系统保留这个task中的activities的每个状态。如果用户resume这个task通过选择启动图标启动task，这个task 变成foreground，然后resume这个task顶部的activity。
- 如果用户按下返回键，当前的activity从stack中pop出来然后destroy。stack中的前一个activity会resume。当activity被destroy，系统不会保留这个activity的状态。
- activities可以被实例化多次，即使是从不同的task中。
<table><tr><td bgcolor=#e1f5fe>
<font color="#01579b" size=4>
Navigation Design:
了解更多的app 的navigation的工作，请看android design's Navigation guide.
https://developer.android.google.cn/guide/navigation/navigation-design-graph
</font>
</td></tr></table>

#### Managing tasks
Android 管理task 和back stack的方法如上所述，所有连续启动的activity在同一个task，保存在一个后入先出的stack里面- 在大多数的app里面工作良好，你不需要担心你的activities怎么跟task关联以及怎么他们是怎么存在于back stack里面的。然而，你可能想要中断这些正常的行为。也许你想要一个activity在一个新的task里面启动而不是放置在当前的task里面。或者 当你想要启动一个activity时，不是去创建一个新的实例而是把已有的实例置顶，或者，你想要用户离开当前task时把除了root activity以外的所有activity清空。
你可以做这些事情或者更多，使用 manifest 文件里面 <activity>的属性或者在你传递给startActivity()方法里面的intent设置flag。
在这个方面，下面一些主要的<activity>的属性你可以使用
- taskAffinity
- launchMode
- allowTaskReparenting
- clearTaskOnLaunch
- alwaysRetainTaskState
- finishOnTaskLaunch
下面时你可以使用的主要的intent 的flag：
- FLAG_ACTIVITY_CLEAR_TOP
- FLAG_ACTIVITY_SINGLE_TOP
- FLAG_ACTIVITY_NEW_TASK
在下面的小节中，你可以看到使用manifest 属性和intent 的flags 去定义activities 是如何关联task以及他们在back stack中的行为。
此外，将分别讨论如何在“最近”屏幕中表示和管理任务和活动的注意事项。查看 [Recents Screen](https://developer.android.google.cn/guide/components/recents.html) 获取更多信息。通常，你应该允许系统定义你的task和activities如何在Recents screen 中显示，你不应该修改这些行为。
- Caution：
大多数app不应该中断activities和task的默认行为，如果你决定修改你的activity的默认行为是必须的，请谨慎使用，并确保在启动过程中以及使用“后退”按钮从其他activity和任务导航回activity时测试activity的可用性。确保测试导航行为不与用户预期行为冲突。
##### Defining launch modes
Launch modes 允许你定义一个新的activity实例如果关联当前task。你可以通过以下两种方式定义不同的launch modes：
- 使用manifest 文件
当你在你的manifest file中定义activity，你可以指定当它启动的时候如果与task 关联。
- 使用 Intent 的flag
当你调用startActivity() 方法时，你可以在intent中添加一个flag，以声明新的activity应如何(或者是否)与当前task 关联。

这样，如果activity A启动activity B，则activity B可以在清单中定义它应如何与当前task关联（如果有的话），activity A也可以请求activity B应该如何与当前task关联。 如果两个activity 都定义了activity B应如何与task关联，则activity A的请求（如Intent 中所定义）优先于activity B的请求（如其manifest中所定义）。
- Note:
某些在manifest 文件中可用的launch modes在Intent的flag中不可用，一些在Intent flag中可用的launch mode不能定义在manifest文件中。
###### Using the manifest file
在manifest文件中声明activity时，可以使用<activity>元素的launchMode属性指定activity应如何与task关联。
launchMode属性指定有关如何将activity启动到task中的指令。 你可以将四种启动模式分配给launchMode属性：
- standard(the default mode)
默认。 系统在task中从中创建活动的新实例，并将intent路由到该活动。 该activity可以被实例化多次，每个实例可以属于不同的task，并且一个task可以具有多个实例。
- singleTop
如果activity的实例已经存在于当前task的顶部，则系统通过调用其onNewIntent()方法将Intent路由到该实例，而不是创建activity的新实例。 该activity可以被实例化多次，每个实例可以属于不同的task，一个task可以具有多个实例（但前提是位于后堆栈顶部的activity不是该activity的现有实例）。
例如，假设task的后stack由根activity A组成，根activity A在顶部，activity B，C和D（stack在A-B-C-D； D在顶部）。 Intent到达类型为D的activity。如果D具有默认的“标准”启动模式，则将启动该类的新实例，并且stack将变为A-B-C-D-D。 但是，如果D的启动模式为“ singleTop”，则D的现有实例通过onNewIntent（）接收Intent，因为它位于stack的顶部，即stack仍为A-B-C-D。 但是，如果到达类型B的activity的Intent，则将B的新实例添加到stack中，即使其启动模式为“singleTop”。Note: 注意：创建activity的新实例时，用户可以按“后退”按钮返回上一个activity。 但是，当activity的现有实例处理新Intent时，用户无法在新Intent到达onNewIntent（）之前按“后退”按钮以返回到activity状态。
- singleTask
系统创建一个新task，并在新task的根目录处实例化activity。 但是，如果activity的实例已经存在于单独的task中，则系统将通过对其onNewIntent（）方法的调用将Intent路由到现有实例，而不是创建新实例。 一次只能存在一个activity实例。注意：尽管activity在新task中开始，但是“后退”按钮仍使用户返回上一个activity
- singleInstance
与“ singleTask”相同，除了系统不会将任何其他activity启动到保存实例的task中。 activity始终在是其task的始终是单例的，只有一个成员。 所有用这种模式打开的activity都会在一个单独的task中。

再举一个例子，Android浏览器应用通过在<activity>元素中指定singleTask启动模式，声明Web浏览器activity应始终在其自己的task中打开。 这意味着，如果您的应用发出打开Android浏览器的意图，则其activity不会与您的应用放在同一task中。 取而代之的是，要么为浏览器启动一个新task，要么，如果浏览器已经在后台运行了一个task，则将该task带到新的位置以处理新的意图。
无论activity是在新task中启动还是在与启动该task的activity相同的task中启动，“后退”按钮始终会将用户带到上一个activity。 但是，如果启动指定singleTask启动模式的activity，则如果该activity的实例存在于后台task中，则整个task都将置于前台。 至此，back stack现在在stack的顶部包括来自提出的task的所有activity。 下图说明了这种情况。
![avatar](./image/diagram_backstack_singletask_multiactivity.png)
具有“singleTask” launch model的activity如何添加到后stack的表示。 如果activity已经是具有自身后退stack的后台task的一部分，则整个back stack也将位于当前task之上。
有关在manifest文件中使用launch mode的更多信息，请参见<activity>元素文档，其中对launchMode属性和接受的值进行了更多讨论。
注意：您可以使用启动模式的Intent所包含的标志来覆盖您使用launchMode属性为activity指定的行为，如下一节所述。
##### Using Intent flags
启动活动时，可以通过在传递给startActivity（）的Intent中包含标志来修改活动与其task的默认关联。 可以用来修改默认行为的flag是：
- FLAG_ACTIVITY_NEW_TASK
在新task中启动activity。 如果您现在正在为该activity运行task，那么该task将被带到前台并恢复其状态，并且该activity将在onNewIntent（）中收到新的Intent。
这产生的行为与上一节中讨论的“ singleTask” launchMode值相同。
- FLAG_ACTIVITY_SINGLE_TOP
如果正在启动的activity是当前activity（在后stack的顶部），则现有实例将收到对onNewIntent（）的调用，而不是创建该activity的新实例。
这产生与上一节中讨论的“ singleTop” launchMode值相同的行为。
- FLAG_ACTIVITY_CLEAR_TOP
如果正在启动的activity已经在当前task中运行，那么与其启动该activity的新实例，不如破坏该activity之上的所有其他activity，并将此Intent传递给该activity的恢复实例（现在 顶部），通过onNewIntent（））。
产生此行为的launchMode属性没有相对应的值。
FLAG_ACTIVITY_CLEAR_TOP最常与FLAG_ACTIVITY_NEW_TASK结合使用。 当这些标志一起使用时，它们是一种在另一个task中定位现有activity并将其置于可以响应Intent的位置的方法。
Note：如果指定activity的启动模式为“标准”，则也会将其从stack中删除，并在其位置启动一个新实例以处理传入的Intent。 这是因为当启动模式为“标准”时，总是为新Intent创建新实例。
##### Handling affinities
affinity指示activity喜欢属于哪个task。 默认情况下，同一应用程序中的所有activity都具有相互关联性。 因此，默认情况下，同一应用程序中的所有activity都希望属于同一task。 但是，您可以修改activity的默认关联性。 在不同应用程序中定义的activity可以共享affinity，或者可以在同一应用程序中定义的activity分配不同的task affinity。
您可以使用<activity>元素的taskAffinity属性修改任何给定activity的 affinity 。
taskAffinity属性采用字符串值，该字符串值必须与<manifest>元素中声明的默认程序包名称唯一，因为系统使用该名称来标识应用程序的默认task关联。
affinity在下面两种情况下起作用：
- 当Intent 启动activity的时候包含了FLAG_ACTIVITY_NEW_TASK flag。
一个新的activity，默认是会启动到调用startActivity()启动它的activity所属的task中。它被push到相同的back stack中。然而当传递给startActivity()的Intent中包含了FLAG_ACTIVITY_NEW_TASK flag 时，系统将寻找不同的task来容纳新的activity，通常，这个是一个新的task。但是这也不是绝对的，如果已经存在一个task与新的task有相同的affinity，则会将改activity启动到该task中，如果没有，那么将开始一个新的task。
如果这个flag导致用户开始了一个新的task，而且用户按下home键离开这个activity，那么用户必须有某种方法可以navigate回到这个task。一些实体，比如通知管理器经常在外部task中启动activity，而不是将其作为自己的一部分，因此他们总是将FLAG_ACTIVITY_NEW_TASK 放入传递给startActivity() 的Intent中。如果你的可以被外部实体唤起的activity可能使用了这个flag唤起的，请注意用户有独立的方式来返回已经启动的任务，例如使用launch icon(task的root activity有 CATEGORY_LAUNCHER intent filter，详情请看[Starting a task](https://developer.android.google.cn/guide/components/activities/tasks-and-back-stack#Starting)).
- 当一个activity带有allowTaskReparenting 属性并且设置为true. 
在这种情况下，activity可以从它启动的task移动到具有关联性的task。
例如，假设将报告选定城市的天气状况的activity定义为旅行应用程序的一部分。 它与同一应用程序中的其他activity具有相同的关联性（默认应用程序关联性），并且允许使用此属性进行重新关联。 当您的一项activity开始天气报告者activity时，它最初与您的activity属于同一task。 但是，当旅行应用程序的task来到前台时，天气报告者activity将重新分配给该task并显示在其中。

tips:
提示：如果从用户的角度来看，一个APK文件包含多个app，则您可能希望使用taskAffinity属性为与每个app相关联的activity分配不同的affinity。
##### Clearing the back stack
如果用户长时间离开task，系统将清除除 root activity以外的所有activity的task。 当用户再次返回task时，仅还原root activity。 系统的行为方式是这样的，因为经过较长时间后，用户可能已经放弃了以前的工作，而返回到task以开始新的工作。
您可以使用一些activity属性来修改此行为：
- alwaysRetainTaskState
如果在任务的root activity 中将此属性设置为“ true”，则不会发生上述默认行为。 即使经过很长时间，该任务也会将所有activity保留在其堆栈中。
- clearTaskOnLaunch
如果在task的root activity中将此属性设置为“ true”，则每当用户离开task并返回到该task时，堆栈都会清除为root activity。 换句话说，它与alwaysRetainTaskState相反。 即使在离开task一小会后，用户也始终以其初始状态返回task。
- finishOnTaskLaunch
此属性类似于clearTaskOnLaunch，但它对单个activity而不是整个task进行操作。 它还可能导致任何activity消失，包括根activity。 设置为“ true”时，activity仅对于当前会话仍是task的一部分。 如果用户离开然后返回task，该task将不再存在。
##### Starting a task
你可以使用 intent filter 的"android.intent.action.MAIN"和category "android.intent.category.LAUNCHER"来设置一个entity的入口，如下所示
```xml
<activity ... >
    <intent-filter ... >
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    ...
</activity>
```
这种intent filter 会在应用程序启动器中创建icon和标签，从而为用户提供启动activity的方法并且在activity 启动后随时返回创建的task的方法。
第二个功能很重要：用户必须能够离开task，然后稍后使用此activity launcher返回该task。因此，当activity 既有ACTION_MAIN和CATEGORY_LAUNCHER filter 时，才使用singleTask 和singleInstance,想象一下，如果缺少过滤器会发生什么：一个Intent启动一个singleTask的activity，启动一个新的task，并且用户花一些时间在该task上工作。然后用户按下home键，现在task已经到background，用户无法返回task，因为该task未在app launcher中显示。
### process and application lifecycle
- JobServiceW
#### Parcelable and Boundle


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
 组件调用 startService() 方法最终会调用 onStartCommand() 方法。当service 启动了，它的生命周期与启动他的组件没有依赖关系，哪怕 启动它的组件被销毁了。

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


























