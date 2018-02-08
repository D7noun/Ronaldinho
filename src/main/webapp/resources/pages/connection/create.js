$(document).ready(function() {
	$('#connectionBeanConnectionPortNumbers').selectize({
		delimiter : ',',
		persist : false,
		create : function(input) {
			return {
				value : input,
				text : input
			}
		}
	});
});