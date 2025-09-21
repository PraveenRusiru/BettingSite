$(document).ready(function () { 
    $('#place-winner-bet-btn').click ( function () {
        // confirmation alert
        let balance = localStorage.getItem("balance");
        let betAmount = $("#betAmount").val();
        let potentialWin = $("#potentialWin").text();
        let stake = parseFloat($(".outcome-winning-odds").text());
        console.log("stake", stake, " betAmount", betAmount, "potentialWin", potentialWin, "username", localStorage.getItem("username"));
        
        
        if(localStorage.getItem("username") === null ) {
            Swal.fire('Error', 'You must be logged in to place a bet.', 'error');
            $(matchDetailModal).removeClass("active");
            loginModal.addClass("active");;
            return;
        }
        if( betAmount === "" || isNaN(betAmount) || parseFloat(betAmount) <= 0) {
            Swal.fire('Error', 'Please enter a valid bet amount.', 'error');
            return;
        }
        if(stake === "" || isNaN(stake) || parseFloat(stake) <= 0) {
            Swal.fire('Error', 'Stake amount is invalid.', 'error');
            return;
        }
        if (balance === null || isNaN(balance) || parseFloat(balance) < parseFloat(betAmount)) { 
            Swal.fire('Error', 'Insufficient balance to place this bet.', 'error');
            return;
        }
        Swal.fire({
            title: 'Confirm Bet',
            text: 'Are you sure you want to place this bet?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Yes, place bet',
            cancelButtonText: 'No, cancel'
        }).then((result) => {
            if (result.isConfirmed) {
                // Show success message
                try { 
                    fetch("http://localhost:8080/bet/placeBet", {
                        method: "POST",
                        headers: {
                            Authorization: "Bearer " + localStorage.getItem("accessToken"),
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                    stake: stake, // Example match ID
                    amount: parseFloat(betAmount),
                    potentialReturn: potentialWin,
                    username: localStorage.getItem("username"),
                  }),
                    })
                    .then((response) => {
                        if (response.ok) {
                            Swal.fire('Bet Placed!', 'Your bet has been placed successfully.', 'success');
                            localStorage.setItem("balance",
                                parseFloat(balance) - parseFloat(betAmount)
                            );
                            window.location.reload();
                        } else {
                            response.json().then((data) => {
                                Swal.fire('Error', data.message || 'There was an error placing your bet.', 'error');
                            });
                        }
                    });
                } catch (error) { }
                  
                  
                  

                
            }
        });
    });
});