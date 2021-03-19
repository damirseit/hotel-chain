var role = 'User';

$.ajax({
	url: 'services/items/role',
	dataType: 'json',
	success: function(r) {
		role = r.response;
	}
});

function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	var expires = "expires="+d.toUTCString();
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

$(document).ready(function() {
	$('.sign-out').on('click', function() {
		$.ajax({
			url: 'services/items/signout',
			dataType: 'json'
		});
	});

	$('#add-occupants').on('click', function() {
		var roomType = $('#room-type').val(), n = 0;
		if(roomType == 'single') {
			n = 0;
		}
		else if(roomType == 'double') {
			n = 1;
		}
		else if(roomType == 'presidental') {
			n = 2;
		}
		$('#occupants').empty();
		for(var i = 0; i < n; i++) {
			$('#occupants').append('<li><input type="textbox" placeholder="Occupant name" style="width: 80%;"></li>');
		}
	});
	$('.brand-title').on('click', function() {
		if(role == 'User') {
			$(location).attr('href', 'index.html');
		}
		else if(role == 'guest'){
			$(location).attr('href', 'main.html');
		}
		else if(role == 'Clerk'){
			$(location).attr('href', 'main-clerk.html');
		}
		else if(role == 'ADMIN'){
			$(location).attr('href', 'main-manager.html');
		}
	});

	$('.contact').on('click', function() {
		$(location).attr('href', 'contact.html');
	});

	$('#user').on('click', function() {
		if(role == "guest") {
			$(location).attr('href', 'account-user.html');
		}
		else if(role == 'Clerk') {
			$(location).attr('href', 'account-clerk.html');
		}
		else if(role == 'ADMIN') {
			$(location).attr('href', 'account-manager.html');
		}
	});

	$('#check-button').on('click', function() {
		var hotelName = $('#hotel-name').val();
		var fromDate = $('#from-date').val();
		var toDate = $('#to-date').val();
		var roomType = $('#room-type').val();
		$.ajax({
			url: 'services/items/check',
			dataType: 'json',
			data: {
				HotelName: hotelName,
				FromDate: fromDate,
				ToDate: toDate,
				RoomType: roomType,
			},
			success: function(r) {
				if(r.response == "no") {
					alert("Booked :(");
				}
				else if(r.response == "yes") {

					alert("This room is not booked. It costs " + r.price);
					$('#reserve-button').css('visibility', 'visible');

					if(roomType != 'single'){
						$('#add-occupants').css('visibility', 'visible');
					}

				}
			}
		});
	});


	$('#reserve-button').on('click', function() {
		if(role == 'User') {
			alert("You have to sign up first!");
		}
		else {
            var list = [];
			$('#occupants li').each(function(i) {
				var item = $(this).children('input').val();
				if(item != ''){
					list.push(item);
				}
			});
			var hotelName = $('#hotel-name').val();
			var fromDate = $('#from-date').val();
			var toDate = $('#to-date').val();
			var roomType = $('#room-type').val();
			$.ajax({
				url: 'services/items/reserve',
				dataType: 'json',
				data: {
					HotelName: hotelName,
					FromDate: fromDate,
					ToDate: toDate,
					RoomType: roomType,
					Names: list
				},
				success: function(r) {
					alert(r.response);
				}
			})
		}
	});
	$('#reservations1').on('click', '.show-bill', function() {
		var tr = $(this).closest('tr');
		var rID = tr.children('td').find('.r-ID').val();
		$.ajax({
			url: 'services/items/showbill',
			dataType: 'json',
			data: {
				rID: rID
			},
			success: function(r) {
				var temp = "";
				r.forEach(function(l) {
					var myTr = '<tr>';
					var i = 0;
					l.forEach(function (item) {
						myTr += '<td>' + item + '</td>';
						i++;
					});
					myTr += '</tr>';
					temp += myTr;
				});
				setCookie("bill", temp, 365);
				window.open('bill.html', '_blank');
			}
		});
	});
});