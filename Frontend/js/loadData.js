$(document).ready(function () { 
    window.usernameSettingsButton = $("#usernameSettingsButton");
    window.emailSettingsButton = $("#emailSettingsButton");
    window.nicSettingsButton = $("#nicSettingsButton");
    window.passwordSettingsButton = $("#passwordSettingsButton");
    window.newPassword = $("#newPassword");
    window.confirmNewPassword = $("#confirmNewPassword");
    window.passwordUpdate = $(".passwordUpdate");
    window.profileId = $("#profileId");
    usernameSettingsButton.on("click", function () {
        profilename.prop("disabled", false);
        profilename.focus();
    });
    emailSettingsButton.on("click", function () {
        profileEmail.prop("disabled", false);
        profileEmail.focus();
    });
    nicSettingsButton.on("click", function () {
        profileNic.prop("disabled", false);
        profileNic.focus();
    });
    passwordSettingsButton.on("click", function () {
        currentPassword.prop("disabled", false);
        currentPassword.focus();
        passwordUpdate.prop("hidden", false);
        newPassword.prop("disabled", false);
        newPassword.focus();
        confirmNewPassword.prop("disabled", false);
        confirmNewPassword.focus();
    });

})
    console.log("loadData is ready");
    
async function fetchAllUserData(username) {
        let tocken =  getStoredToken();
    console.log("access token ", tocken);
    if (tocken === null) { 
        console.error("Access token is null. User might not be authenticated.");
        return;
    }
      try {
        const response = await fetch(
          `http://localhost:8080/userData/getAll?username=${username}`,
          {
            headers: {
              Authorization: "Bearer " +tocken,
            },
            ContentType: "application/json",
          }
        );
          if (response.ok) {
                const data = await response.json();
        
          console.log("Fetched user data:", data);
          console.log("Username:", data.data.username);
          console.log("Email:", data.data.email);
          profileUserName.text(data.data.username);
              profileUserEmail.text(data.data.email);
              profileId.text(data.data.id);
              profilename.val(data.data.username);
            //   profilename.style.disable = true;
              profileEmail.val(data.data.email);
            //   profileEmail.style.disable = true;
              profileNic.val(data.data.nic);
            //   profileNic.style.disable = true;
        // }      
          } else {
              console.error("Failed to fetch user data. Status:", response.status);
              // localStorage.setItem("accessToken", await getAccessToken());
            //   await fetchAllUserData(username);
          }
        
      } catch (error) {
        console.error("error", error);
      }
    }

async function loadMatchdata() {
   
      // let token = getStoredToken();
      // console.log("access token ", token);
      // if (token === null) {
      //   console.error("Access token is null. User might not be authenticated.");
      //   return;
      // }
  
    try {
      const response = await fetch(`http://localhost:8080/match/live`, {
        headers: {
          // Authorization: "Bearer " + token,
          "Content-Type": "application/json",
        },
      });
      const data = await response.json();

      if (response.ok) {
        return data; // already parsed JSON
      } else {
        console.error("Failed to fetch user data. Status:", response.status);
        return null;
      }
    } catch (error) {
      console.error("error", error);
      return null;
    }
  
}

async function loadUpcomingMatchdata() {
    try {
      const response = await fetch(`http://localhost:8080/match/upcoming`, {
        headers: {
          // Authorization: "Bearer " + token,
          "Content-Type": "application/json",
        },
      });
      const data = await response.json();

      if (response.ok) {
        return data; // already parsed JSON
      } else {
        console.error("Failed to fetch user data. Status:", response.status);
        return null;
      }
    } catch (error) {
      console.error("error", error);
      return null;
    } 
 }




// })