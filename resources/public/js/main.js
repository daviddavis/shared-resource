$(function() {
  $(".date-picker").datepicker({dateformat: "yyyy-mm-dd"});

  $(".date-time-picker").datetimepicker({dateFormat: "yyyy-mm-dd hh:MM TT", 
					 showButtonPanel: true,
                                         minDate: new Date()});
});
