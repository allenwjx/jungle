/**
 * UI Tree
 */
var UITree = function() {
	var loadTreeData = function(id, data) {
		$("#" + id).jstree({
			"core" : {
				"themes" : {
					"responsive" : false
				},
				"check_callback" : true,
				'data' : data
			},
			"types" : {
				"#" : {
              		"max_children" : 1,
              		"max_depth" : 2
            	},
            	"domain" : {
            		"icon" : "icon-globe icon-state-info icon-md"
            	}, 
				"default" : {
					"icon" : "fa fa-folder icon-state-info icon-md"
				},
				"file" : {
					"icon" : "fa fa-file-code-o icon-state-info icon-md"
				}
			},
			"state" : {
				"key" : "tianqiscript"
			},
			"plugins" : [ "wholerow","contextmenu", "state", "types", "search", "sort"],
			
		});
	}
	
	return {
		init: function(id, data) {
			loadTreeData(id, data);
		},

		layoutInit: function() {
			var pageContentWidth = $('.page-content').css('min-height');
	   		var treeHeight = pageContentWidth.substring(0, pageContentWidth.length - 2) - 10;
	   		$('.page-tree-content').css("height", (treeHeight + 'px'));
		}
	}
}();
