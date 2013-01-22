var base_url = "http://swe.cmpe.boun.edu.tr:8280";
var db_addr = {
	 admin :"{base_url}/json/admin.json"
	,comment: "{base_url}/json/comment.json"
	,handicap_type: "{base_url}/json/handicap_type.json"
	,location: "{base_url}/json/location.json"
	,location_tag: "{base_url}/json/location_tag.json"
	,person: "{base_url}/json/person.json"
	,proof: "{base_url}/json/proof.json"
	,tag: "{base_url}/json/tag.json"
	,tag_handicap_type: "{base_url}/json/tag_handicap_type.json"
	,tag_type: "{base_url}/json/tag_type.json"
	,tag_violation: "{base_url}/json/tag_violation.json"
	,user:"{base_url}/json/user.json"
	,violation: "{base_url}/json/violation.json"
	,violation_report: "{base_url}/json/violation_report.json"
	,g: function(tmpl){
		return tmpl.replace("{base_url}",base_url);
	}
};

(function($) {
      
        var app = $.sammy('#main', function() {
		
		  this.use('Template');
      
          this.get('#/', function(context) {
		  context.app.swap('');
            this.load(db_addr.g(db_addr.violation_report))
            .then(function(items) {
				this.load(db_addr.g(db_addr.proof)).then(function(proofs){
				    var proofs_arr = Array("","","","","","","","","","","","");
					var proof_url = "";
					$.each(proofs, function(i, proof) {
						if(!proofs_arr[proof.VIOLATION_REPORT_ID]){
							proofs_arr[proof.VIOLATION_REPORT_ID] = proof.URL;
						}
					});
					$.each(items, function(i, x) {
						proof_url = "";
					  if(proofs_arr[x.ID])
						proof_url = proofs_arr[x.ID];
					  context.render('template/violation.template', {item: x, proof: proof_url}).appendTo(context.$element());
					});
				});
            });
          });
      
        });
      
        $(function() {
          app.run('#/');
        });
      
})(jQuery);