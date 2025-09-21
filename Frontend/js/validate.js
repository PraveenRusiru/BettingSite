
$(document).ready(function () {
  console.log("Document is ready");
  let usernameRegex = /^[a-zA-Z0-9_]{3,16}$/;
  let passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  let nicRegex = /^(?:\d{12}|\d{9}[VvXx])$/;
  let slPhoneRegex = /^(?:\+94|0)[1-9]\d{8}$/;
  let usernameInput = $("#signupUsername");
  let confirmPasswordInput = $("#confirmPassword");
  let signupPassword = $("#signupPassword");
  let signupNIC = $("#signupNIC");
  let signupEmail = $("#signupEmail");
  let usernameFeedback = $("#usernameFeedback");
  let emailFeedback = $("#emailFeedback");
  let nicFeedback = $("#nicFeedback");
  let passwordFeedback = $("#passwordFeedback");
  let confirmPasswordFeedback = $("#confirmPasswordFeedback");
  let signupForm = $("#signupForm");
  // const loginBtn = $("#loginBtn");
  let isMailOk = false;
  let isusernameOk = false;
  let isNicOk = false;
  let isPasswordOk = false;
  let isConfirmPasswordOk = false;
  window.signupModel = $("#signupModal");
  window.loginModalJq = $("#loginModal");
  // let profileModal = $("#profileModal");
  let loginUsername = $("#loginUsername");
  let loginPassword = $("#loginPassword");
  let loginUsernameFeedback = $("#loginUsernameFeedback");
  let loginPasswordfeedback = $("#loginPasswordfeedback");
  let isLoginUsernameOk = false;

  let isUpdateUsernameOk = false;
  let isUpdateMailOk = false;
  let isUpdateNicOk = false;
  let isCurrentPasswordOk = false;
  let isNewUpdatePasswordOk = false;
  let isUpdateConfirmPasswordOk = false;
  // DOM elements
  window.loginBtn = $("#loginBtn");
  window.signupBtn = $("#signupBtn");
  window.loginModal = $("#loginModal");
  window.signupModal = $("#signupModal");
  window.twoFAModal = $("#twoFAModal");
  window.closeLoginModal = $("#closeLoginModal");
  window.closeSignupModal = $("#closeSignupModal");
  window.closeTwoFAModal = $("#closeTwoFAModal");
  window.switchToSignup = $("#switchToSignup");
  window.switchToLogin = $("#switchToLogin");
  window.matchDetailModal = $("#matchDetailModal");
  window.closeMatchModal = $("#closeMatchModal");
   window.liveMatchesContainer = $("#liveMatches");
  window.upcomingMatchesContainer = $("#upcomingMatches");
  window.betAmountInput = $("#betAmount"); // jQuery object
  window.potentialWinSpan = $("#potentialWin");
  window.betTabs = $(".bet-tab");
  window.googleLoginBtn = $("#googleLoginBtn");
  window.googleSignupBtn = $("#googleSignupBtn");
  window.verify2FABtn = $("#verify2FABtn");
  window.resendCode = $("#resendCode");
  window.manage2FABtn = $("#manage2FABtn");
  window.profileUserName = $("#profileUserName");
  window.profileUserEmail = $("#profileUserEmail");
  window.profilename = $("#profilename");
  window.profileEmail = $("#profileEmail");
  window.profileNic = $("#profileNic");
  window.currentPassword = $("#currentPassword");
  window.confirmNewPassword = $("#confirmNewPassword");
  // Profile and logout
  window.profileBtn = $("#profileBtn");
  window.logoutBtn = $("#logoutBtn");
  window.userInfo = $("#userInfo");
  window.userName = $("#userName");
  window.userAvatar = $("#userAvatar");
  window.newPassword = $("#newPassword");
  // Profile modal
  window.profileModal = $("#profileModal");
  window.closeProfileModal = $("#closeProfileModal");
  window.profileForm = $("#profileForm");
  isLoggedIn = localStorage.getItem("isLoggedIn");
  window.balance = $("#balance");
  if (isLoggedIn === "true") {
    // Update UI to logged-in state
    console.log("User is logged in",isLoggedIn,accessToken,localStorage.getItem("username"));
    updateAuthUI(true);
  } else {
    // Ensure UI shows logged-out state
    updateAuthUI(false);
  }

  

  loginModalJq.on("submit", function (event) {
    event.preventDefault();
    $("#loginBtnLoginModel").html(
      '<i class="fas fa-spinner fa-spin"></i> Processing...'
    );
    console.log("Checking validation for login", isLoginUsernameOk);
    if (isLoginUsernameOk) {
      fetch(`http://localhost:8080/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          password: loginPassword.val(),
          username: loginUsername.val(),
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          setTimeout(() => {
            // updateAuthUI(false);
            if (data) {
              console.log(
                "Login response:",
                data.data.accessToken,
                data.data.refreshToken
              );
              localStorage.setItem("accessToken", data.data.accessToken);
              localStorage.setItem("refreshToken", data.data.refreshToken);
              localStorage.setItem("username", data.data.username);
              localStorage.setItem("isLoggedIn", isLoggedIn);
              localStorage.setItem("balance", data.data.balance);
              localStorage.setItem("userId", data.data.userId);
              localStorage.setItem("email", data.data.email);

              Swal.fire({
                title: "Success",
                text: "Login successful!",
                icon: "success",
                confirmButtonText: "OK",
              }).then(() => {
                loginModalJq.removeClass("active");
                updateAuthUI(true);
                // 1609 for enable 2fa
                clearAll();
                // Simple reload
                window.location.reload();
              });
            } else {
              updateAuthUI(false);
              Swal.fire({
                title: "Error",
                text: "Login failed. Please try again.",
                icon: "error",
                confirmButtonText: "OK",
              });
            }
          }, 3000);
        });
    } else {
      Swal.fire({
        title: "Invalid Input",
        text: "Enter valid values !!",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  });

  profileForm.on("submit", function (event) {
    event.preventDefault();
    console.log(
      "Checking validation for profile update isUpdateConfirmPasswordOk",
      isUpdateUsernameOk,
      "isUpdateMailOk",
      isUpdateMailOk,
      "isUpdateNicOk",
      isUpdateNicOk,
      "isCurrentPasswordOk",
      isCurrentPasswordOk,
      "isNewUpdatePasswordOk",
      isNewUpdatePasswordOk,
      "isUpdateConfirmPasswordOk",
      isUpdateConfirmPasswordOk
    );
    if (
      isUpdateUsernameOk ||
      isUpdateMailOk ||
      isUpdateNicOk ||
      isCurrentPasswordOk ||
      isNewUpdatePasswordOk ||
      isUpdateConfirmPasswordOk
    ) {
      fetch(`http://localhost:8080/userData/updateData`, {
        method: "PUT",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("accessToken"),
          "Content-Type": "application/json",
        },

        body: JSON.stringify({
          id:profileId.text(),
          oldUsername: profileUserName.text(),
          username: isUpdateUsernameOk ? profilename.val() : null,
          email: isUpdateMailOk ? profileEmail.val() : null,
          nic: isUpdateNicOk ? profileNic.val() : null,
          newPassword: isNewUpdatePasswordOk ? newPassword.val() : null,
          confirmnewPassword: isUpdateConfirmPasswordOk
            ? confirmNewPassword.val()
            : null,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Profile update response:", data);
          // updateAuthUI(false);
          if (data) {
            localStorage.removeItem("username");
            localStorage.setItem("username", profilename.val());
            console.log("profile name ", localStorage.getItem("username"));
            Swal.fire({
              title: "Success",
              text: "Profile update successful!",
              icon: "success",
              confirmButtonText: "OK",
            }).then(() => {
              profileModal.removeClass("active");
              clearAllUpdateData();
              // location.reload();
            });
          } else {
            Swal.fire({
              title: "Error",
              text: "Profile update failed. Please try again.",
              icon: "error",
              confirmButtonText: "OK",
            });
          }
        })
        .catch((error) => {
          console.error("Error during profile update:", error);
          Swal.fire({
            title: "Error",
            text: "An error occurred during profile update. Please try again.",
            icon: "error",
            confirmButtonText: "OK",
          });
        });
    } else {
      Swal.fire({
        title: "Invalid Input",
        text: "Enter valid values !!",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  });

  signupForm.on("submit", function (event) {
    event.preventDefault(); // Prevent form submission
     $("#createAccountBtn").html(
       '<i class="fas fa-spinner fa-spin"></i> Processing...'
     );
    console.log(
      "checking validation ",
      isMailOk,
      isNicOk,
      isusernameOk,
      isPasswordOk,
      isConfirmPasswordOk
    );
    if (
      isMailOk &&
      isNicOk &&
      isusernameOk &&
      isPasswordOk &&
      isConfirmPasswordOk
    ) {
      fetch(`http://localhost:8080/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: usernameInput.val(),
          password: signupPassword.val(),
          confirmPassword: confirmPasswordInput.val(),
          email: signupEmail.val(),
          role: "USER",
          nic: signupNIC.val(),
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Registration response:", data.data);
          updateAuthUI(false);
          if (data) {
            Swal.fire({
              title: "Success",
              text: "Registration successful!",
              icon: "success",
              confirmButtonText: "OK",
            }).then(() => {
              signupModel.removeClass("active");
              loginModal.classList.add("active");
              clearAll();
              
            });
          } else {
            Swal.fire({
              title: "Error",
              text: "Registration failed. Please try again.",
              icon: "error",
              confirmButtonText: "OK",
            });
          }
        });
    } else {
      Swal.fire({
        title: "Invalid Input",
        text: "Enter valid values !!",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  });

  usernameInput.on("keyup", function () {
    var username = $(this).val();

    if (username.length < 3) {
      usernameFeedback.css("color", "red");
      usernameFeedback.text("Username must be at least 3 characters long.");
      isusernameOk = false;
    } else {
      if (username.match(usernameRegex) === null) {
        usernameFeedback.css("color", "red");
        usernameFeedback.text(
          "Accepted : 3–16 characters, letters, numbers, underscores, no spaces"
        );
        isusernameOk = false;
        console.log("checking username regex");
      } else {
        checkUsernameExist(username).then((exists) => {
          if (exists) {
            usernameFeedback
              .css("color", "red")
              .text("Username already exists.");
            isusernameOk = false;
          } else {
            usernameFeedback.css("color", "green").text("Username is valid.");
            isusernameOk = true;
          }
        });
      }
    }
  });

  profilename.on("keyup", function () {
    var updatename = $(this).val();
    if (updatename.length < 3) {
      $("#usernameupdateFeedback").css("color", "red");
      $("#usernameupdateFeedback").text(
        "Username must be at least 3 characters long."
      );
      isUpdateUsernameOk = false;
    } else {
      if (updatename.match(usernameRegex) === null) {
        $("#usernameupdateFeedback").css("color", "red");
        $("#usernameupdateFeedback").text(
          "Accepted : 3–16 characters, letters, numbers, underscores, no spaces"
        );
        isUpdateUsernameOk = false;
        console.log("checking username regex");
      } else {
        checkUsernameExist(updatename).then((exists) => {
          if (exists) {
            $("#usernameupdateFeedback")
              .css("color", "red")
              .text("Username already exists.");
            isUpdateUsernameOk = false;
          } else {
            $("#usernameupdateFeedback")
              .css("color", "green")
              .text("Username is valid.");
            isUpdateUsernameOk = true;
          }
        });
      }
    }
   });

  signupEmail.on("keyup", function () {
    var email = $(this).val();

    if (email.match(emailRegex) === null) {
      emailFeedback.css("color", "red");
      emailFeedback.text("Matches most emails like user.name@example.com");
      isMailOk = false;
    } else {
      checkEmailExist(email).then((exists) => {
        if (exists) {
          emailFeedback.css("color", "red").text("Email already exists.");
          isMailOk = false;
        } else {
          emailFeedback.css("color", "green").text("Email is valid.");
          isMailOk = true;
        }
      });
    }
  });

  profileEmail.on("keyup", function () { 
    var updateEmail = $(this).val();

    if (updateEmail.match(emailRegex) === null) {
      $("#emailupdateFeedback").css("color", "red");
      $("#emailupdateFeedback").text(
        "Matches most emails like user.name@example.com"
      );
      isUpdateMailOk = false;
    } else {
      checkEmailExist(updateEmail).then((exists) => {
        if (exists) {
          $("#emailupdateFeedback")
            .css("color", "red")
            .text("Email already exists.");
          isUpdateMailOk = false;
        } else {
          $("#emailupdateFeedback")
            .css("color", "green")
            .text("Email is valid.");
          isUpdateMailOk = true;
        }
      });
    }
  });


  signupNIC.on("keyup", function () {
    var nic = $(this).val();

    if (nic.match(nicRegex) === null) {
      nicFeedback.css("color", "red");
      nicFeedback.text(
        "If old NIC :  9 digits + final letter V or X (case-insensitive) \n If new NIC : 12 digits"
      );
      isNicOk = false;
    } else {
      checkNicExist(nic).then((exists) => {
        if (exists) {
          nicFeedback.css("color", "red").text("NIC already exists.");
          isNicOk = false;
        } else {
          nicFeedback.css("color", "green").text("NIC is valid.");
          isNicOk = true;
        }
      });
    }
  });

  profileNic.on("keyup", function () { 
      var updatenic = $(this).val();

      if (updatenic.match(nicRegex) === null) {
        $("#nicupdateFeedback").css("color", "red");
        $("#nicupdateFeedback").text(
          "If old NIC :  9 digits + final letter V or X (case-insensitive) \n If new NIC : 12 digits"
        );
        isUpdateNicOk = false;
      } else {
        checkNicExist(updatenic).then((exists) => {
          if (exists) {
            $("#nicupdateFeedback")
              .css("color", "red")
              .text("NIC already exists.");
            isUpdateNicOk = false;
          } else {
            $("#nicupdateFeedback").css("color", "green").text("NIC is valid.");
            isUpdateNicOk = true;
          }
        });
      }
  });

  signupPassword.on("keyup", function () {
    var password = $(this).val();

    if (password.match(passwordRegex) === null) {
      passwordFeedback.css("color", "red");
      passwordFeedback.text(
        "At least 8 characters , 1 lowercase ,  1 uppercase , 1 number , 1 special character(@$!%*?&)"
      );
      isPasswordOk = false;
    } else {
      // if (checkNicExist()) {
      //   passwordFeedback.css("color", "red");
      //   passwordFeedback.text("Email already exists.");
      // } else {
      passwordFeedback.css("color", "green");
      passwordFeedback.text("Password is valid.");
      isPasswordOk = true;
      // }
    }
  });
  newPassword.on("keyup", function () { 
    var updateNewPassword = $(this).val();

    if (updateNewPassword.match(passwordRegex) === null) {
      $("#checkNewPasswordFeedback").css("color", "red");
      $("#checkNewPasswordFeedback").text(
        "At least 8 characters , 1 lowercase ,  1 uppercase , 1 number , 1 special character(@$!%*?&)"
      );
      isNewUpdatePasswordOk = false;
    } else {
      $("#checkNewPasswordFeedback").css("color", "green");
      $("#checkNewPasswordFeedback").text("Password is valid.");
      isNewUpdatePasswordOk = true;
      // }
    }
  });



  currentPassword.on("keyup", function () {
    console.log("Checking current password");
    var currentPasswordVal = $(this).val();
    chcekCurrentPassword(currentPasswordVal).then((exists) => {
      if (exists) {
        $("#checkPasswordFeedback")
          .css("color", "green")
          .text("Current password is correct.");
        isCurrentPasswordOk = true;
      } else {
        $("#checkPasswordFeedback")
          .css("color", "red")
          .text("Current password is incorrect.");
        isCurrentPasswordOk = false;
      }
    });
  });

  confirmNewPassword.on("keyup", function () { 
    var confirmNewPasswordVal = $(this).val();

    if (confirmNewPasswordVal !== newPassword.val()) {
      $("#checkNewConfirmPasswordFeedback").css("color", "red");
      $("#checkNewConfirmPasswordFeedback").text(
        "Confirm password doesn't match"
      );
      isConfirmPasswordOk = false;
    } else {
      $("#checkNewConfirmPasswordFeedback").css("color", "green");
      $("#checkNewConfirmPasswordFeedback").text("Confirm password matches.");
      isConfirmPasswordOk = true;
    }
  });
  confirmPasswordInput.on("keyup", function () {
    var confirmPassword = $(this).val();

    if (confirmPassword !== signupPassword.val()) {
      confirmPasswordFeedback.css("color", "red");
      confirmPasswordFeedback.text("Confirm password doesn't match");
      isConfirmPasswordOk = false;
    } else {
      // if (checkNicExist()) {
      //   passwordFeedback.css("color", "red");
      //   passwordFeedback.text("Email already exists.");
      // } else {
      confirmPasswordFeedback.css("color", "green");
      confirmPasswordFeedback.text("Confirm password matches.");
      isConfirmPasswordOk = true;
      // }
    }
  });

  loginUsername.on("keyup", function () {
    var loginusername = $(this).val();
    if (loginusername.match(usernameRegex) === null) {
      loginUsernameFeedback.css("color", "red");
      loginUsernameFeedback.text(
        "Accepted : 3–16 characters, letters, numbers, underscores, no spaces"
      );
      isLoginUsernameOk = false;
    } else {
      checkUsernameExist(loginusername).then((exists) => {
        if (exists) {
          loginUsernameFeedback
            .css("color", "green")
            .text("Username is registered.");
          isLoginUsernameOk = true;
        } else {
          loginUsernameFeedback
            .css("color", "green")
            .text("Username is unregistered.");
          isLoginUsernameOk = false;
        }
      });
    }
  });

  function checkUsernameExist(username) {
    console.log("Checking username existence");
    return fetch(
      `http://localhost:8080/auth/validateUsername?username=${username}`
    )
      .then((response) => response.json())
      .then((data) => {
        return data.data; // Assuming data.data is true if username exists
      })
      .catch((error) => {
        console.error("Error checking username:", error);
        return false;
      });
  }

  function checkEmailExist(email) {
    return fetch(`http://localhost:8080/auth/validateMail?email=${email}`)
      .then((response) => response.json())
      .then((data) => {
        return data.data;
      })
      .catch((error) => {
        console.error("Error checking email:", error);
      });
  }

  function checkNicExist(nic) {
    return fetch(`http://localhost:8080/auth/validateNIC?nic=${nic}`)
      .then((response) => response.json())
      .then((data) => {
        return data.data;
      })
      .catch((error) => {
        console.error("Error checking email:", error);
      });
  }
  function chcekCurrentPassword(password) {
    console.log(localStorage.getItem("accessToken"));
    return fetch(`http://localhost:8080/userData/checkPassword`, {
      method: "POST",
      // headers: {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("accessToken"),
        "Content-Type": "application/json",
      },
      // ContentType: "application/json",
      // },
      body: JSON.stringify({
        password: password,
        username: localStorage.getItem("username"),
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        return data.data;
      })
      .catch((error) => {
        console.error("Error checking email:", error);
      });
  }
  function clearAll() {
    usernameInput.val("");
    signupPassword.val("");
    confirmPasswordInput.val("");
    signupEmail.val("");
    signupNIC.val("");
    usernameFeedback.text("");
    emailFeedback.text("");
    nicFeedback.text("");
    passwordFeedback.text("");
    confirmPasswordFeedback.text("");
  }
  
});
let isLoggedIn = "";
let accessToken = getStoredToken();
window.updateAuthUI = function (isLoggedIn, userData = null) {
  if (isLoggedIn && accessToken != null) {
    // User is logged in
    loginBtn.hide();
    signupBtn.hide();

    profileBtn.css("display", "flex");
    logoutBtn.css("display", "flex");
    userInfo.css("display", "flex");
    balance.text("Balance: $"+ (localStorage.getItem("balance") || "0.00"));

    // twoFAModal.addClass("active");
    localStorage.setItem("isLoggedIn", true);
    if (userData) {
      userName.text(userData.name || "User");
      userAvatar.attr(
        "src",
        userData.avatar || "https://via.placeholder.com/40x40"
      );
    }
    
  } else {
    // User is not logged in
    loginBtn.show();
    signupBtn.show();
    profileBtn.hide();
    logoutBtn.hide();
    userInfo.hide();
  }
};
function getStoredToken() {
  console.log("Retrieving stored token");
  return localStorage.getItem("accessToken");
}

async function getAccessToken() {
  let token = getStoredToken();
  console.log("access tocken in getAccessToken() ",token);
  // if (!token) {
    // try refresh
    const response = await fetch("http://localhost:8080/auth/refresh",{
      method: "POST",
      credentials: "include", // important: send cookies
    });
    const data = await response.json();
    console.log("Refresh response:", data.code);
    if (data.code===200) {
    console.log("Token refreshed successfully", data.accessToken);
      token = data.accessToken;
      localStorage.setItem("accessToken", token);
    } else {
    // refresh failed → force logout
    console.log("Token refresh failed, logging out ",data.code);
      // localStorage.removeItem("accessToken");
      token = getStoredToken();
    }
  // }

  return token;
}

function clearAllUpdateData() {
  $("#usernameupdateFeedback").text("");
  $("#emailupdateFeedback").text("");
  $("#nicupdateFeedback").text("");
  $("checkPasswordFeedback").text("");
  $("checkNewPasswordFeedback").text("");
  $("#checkNewConfirmPasswordFeedback").text("");
}


  
    
