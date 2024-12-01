package com.example.planetze86;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    LoginActivityView view;
    @Mock
    LoginActivityModel model;

    @Test
    public void forgotPass_click(){

        LoginActivityPresenter presenter = new LoginActivityPresenter(view, model);
        presenter.onForgotPasswordClicked();
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(view).startActivity(intentCaptor.capture());
        Intent capturedIntent = intentCaptor.getValue();
        assertNotNull(capturedIntent);

    }

    @Test
    public void backbutton_click(){
       LoginActivityPresenter presenter = new LoginActivityPresenter(view,model);
       presenter.onBackButtonClicked();
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(view).startActivity(intentCaptor.capture());
        Intent capturedIntent = intentCaptor.getValue();
        assertNotNull(capturedIntent);

    }
  @Test
   public void signup_click(){
       LoginActivityPresenter presenter = new LoginActivityPresenter(view,model);
       presenter.onSignupButtonClicked();
       ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
       verify(view).startActivity(intentCaptor.capture());
       Intent capturedIntent = intentCaptor.getValue();
       assertNotNull(capturedIntent);

   }
    @Test
    public void testlogin_0(){

        LoginActivityPresenter presenter = new LoginActivityPresenter(view,model);
        presenter.loginUser("email@look.com","");
        verify(view).setOutput("Please enter your email and password");
    }
   @Test

    public void testlogin_1(){

       LoginActivityPresenter presenter = new LoginActivityPresenter(view,model);
       presenter.loginUser("","1234567");
       verify(view).setOutput("Please enter your email and password");
   }


    @Test
    public void testlogin_2() {
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, model);

        // Mock the model's behavior for loginUser
        Mockito.doAnswer(invocation -> {
            LoginActivityModel.LoginCallback callback = invocation.getArgument(2);
            callback.onFailure("Invalid email or password");
            return null;
        }).when(model).loginUser(anyString(), anyString(), any(LoginActivityModel.LoginCallback.class));

        presenter.loginUser("email@look.com", "wrongpassword");

        // Assert
        verify(view).setOutput("Invalid email or password");
    }

    @Test
    public void testlogin_3() {
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, model);

        // Mock the model's behavior for loginUser
        Mockito.doAnswer(invocation -> {
            LoginActivityModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess(true);
            return null;
        }).when(model).loginUser(anyString(), anyString(), any(LoginActivityModel.LoginCallback.class));

        presenter.loginUser("email@look.com", "password123");

        // Assert navigation to Onboarding
        verify(view).startActivity(any(Intent.class));
        verify(view).finish();
    }

    @Test
    public void testlogin_4() {
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, model);

        // Mock the model's behavior for loginUser
        Mockito.doAnswer(invocation -> {
            LoginActivityModel.LoginCallback callback = invocation.getArgument(2);
            callback.onSuccess(false);
            return null;
        }).when(model).loginUser(anyString(), anyString(), any(LoginActivityModel.LoginCallback.class));

        presenter.loginUser("email@look.com", "password123");

        // Assert navigation to EcoTracker
        verify(view).startActivity(any(Intent.class));
        verify(view).finish();
    }


    @Test
    public void testlogin_5() {
        LoginActivityPresenter presenter = new LoginActivityPresenter(view, model);

        // Mock the model's behavior for loginUser
        Mockito.doAnswer(invocation -> {
            LoginActivityModel.LoginCallback callback = invocation.getArgument(2);
            callback.onEmailNotVerified();
            return null;
        }).when(model).loginUser(anyString(), anyString(), any(LoginActivityModel.LoginCallback.class));

        presenter.loginUser("email@look.com", "password123");

        // Assert email not verified message and sign out
        verify(view).setOutput("Please verify your email before logging in.");
        verify(model).signOut();
    }

}



