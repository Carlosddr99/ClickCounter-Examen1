package es.ulpgc.eite.cleancode.clickcounter.counter;

import android.util.Log;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.cleancode.clickcounter.app.AppMediator;
import es.ulpgc.eite.cleancode.clickcounter.app.ClicksToCounterState;
import es.ulpgc.eite.cleancode.clickcounter.app.CounterToClicksState;

public class CounterPresenter implements CounterContract.Presenter {

  public static String TAG = CounterPresenter.class.getSimpleName();

  private WeakReference<CounterContract.View> view;
  private CounterState state;
  private CounterContract.Model model;



  private AppMediator mediator;

  public CounterPresenter(AppMediator mediator) {
    this.mediator = mediator;
    state = mediator.getCounterState();
  }


  @Override
  public void onStart() {
    // Log.e(TAG, "onStart()");

    // initialize the state if is necessary
    if (state == null) {
      state = new CounterState();
    }

    // call the model and update the state
    state.counter=0;
    state.data = ""+0;
    state.clicks=0;
    view.get().primerMomentoClickNotEnable(false);


    /*
    // use passed state if is necessary
    PreviousToCounterState savedState = getStateFromPreviousScreen();
    if (savedState != null) {

      // update the model if is necessary
      model.onDataFromPreviousScreen(savedState.data);

      // update the state if is necessary
      state.data = savedState.data;
    }
    */
  }

  @Override
  public void onRestart() {
    Log.e(TAG, "onRestart()");

    // update the model if is necessary

    state.data=""+state.counter;

    model.onRestartScreen(state.data);
  }

  @Override
  public void onResume() {
     Log.e(TAG, "onResume()");

    // use passed state if is necessary
    ClicksToCounterState savedState = getStateFromNextScreen();

    if (savedState != null) {

      // update the model if is necessary
      if(savedState.data==true){
        state.clicks=0;
      }

      // update the state if is necessary


    }

    // call the model and update the state
    //state.data = model.getStoredData();

    // update the view

    view.get().onDataUpdated(state);

  }


  @Override
  public void onBackPressed() {
    // Log.e(TAG, "onBackPressed()");
  }

  @Override
  public void onPause() {
    // Log.e(TAG, "onPause()");
  }

  @Override
  public void onDestroy() {
    // Log.e(TAG, "onDestroy()");
  }

  @Override
  public void onClicksPressed() {
    // Log.e(TAG, "onClicksPressed()");

    CounterToClicksState pasarAClick=new CounterToClicksState();
    pasarAClick.data=""+state.clicks;
    passStateToNextScreen(pasarAClick);

    view.get().navigateToNextScreen();

  }

  @Override
  public void onResetPressed() {
    // Log.e(TAG, "onResetPressed()");
    state.counter=0;
    state.data=""+state.counter;
    view.get().onDataUpdated(state);
  }

  @Override
  public void onIncrementPressed() {
    // Log.e(TAG, "onIncrementPressed()");
    state.clicks=state.clicks+1;
    if(state.counter==9){
      state.counter=0;
    }else{
      state.counter=state.counter+1;
    }
    state.data=""+state.counter;
    view.get().primerMomentoClickNotEnable(true);
    view.get().onDataUpdated(state);

  }

  private void passStateToNextScreen(CounterToClicksState state) {
    mediator.setCounterNextScreenState(state);
  }

  private ClicksToCounterState getStateFromNextScreen() {
    return mediator.getCounterNextScreenState();
  }

  @Override
  public void injectView(WeakReference<CounterContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(CounterContract.Model model) {
    this.model = model;
  }

}
