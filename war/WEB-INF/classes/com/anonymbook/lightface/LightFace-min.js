
var anonym = -1;
$('body').mousemove( function(e) {  anonym = e.pageY;  }  );
function getAnonymTop(def, boxOffSet) {
	if(anonym < 0) { return def; }
	return Math.abs(anonym - boxOffSet);
}

function getAnonymRealTop(boxSize) {
	
	var clientHeight = FB.Canvas.getPageInfo().clientHeight;
	var scrollTop = FB.Canvas.getPageInfo().scrollTop;
	
	var windowSize = window.getSize();
	var scrollSize = window.getScroll();
	
	if(!clientHeight || clientHeight == 0) {
		clientHeight = windowSize.y;
	}
	
	if(!scrollTop || scrollTop == 0) {
		scrollTop = scrollSize.y;
	}
	
	return scrollTop + ((clientHeight - boxSize) / 2);
}

/* Api LightFace */
var LightFace=new Class({Implements:[Options,Events],options:{width:'auto',height:'auto',draggable:false,title:'',buttons:[],fadeDelay:400,fadeDuration:400,keys:{esc:function(){this.close();}},content:'<p>Message not specified.</p>',zIndex:9001,pad:100,overlayAll:false,constrain:false,resetOnScroll:true,baseClass:'lightface',errorMessage:'<p>The requested file could not be found.</p>'},initialize:function(options){this.setOptions(options);this.state=false;this.buttons={};this.resizeOnOpen=true;this.ie6=typeof document.body.style.maxHeight=="undefined";this.draw();},draw:function(){this.box=new Element('table',{'class':this.options.baseClass,styles:{'z-index':this.options.zIndex,opacity:0},tween:{duration:this.options.fadeDuration,onComplete:function(){if(this.box.getStyle('opacity')==0){this.box.setStyles({top:-9000,left:-9000});}}.bind(this)}}).inject(document.body,'bottom');var verts=['top','center','bottom'],hors=['Left','Center','Right'],len=verts.length;for(var x=0;x<len;x++){var row=this.box.insertRow(x);for(var y=0;y<len;y++){var cssClass=verts[x]+hors[y],cell=row.insertCell(y);cell.className=cssClass;if(cssClass=='centerCenter'){this.contentBox=new Element('div',{'class':'lightfaceContent',styles:{width:this.options.width}});cell.appendChild(this.contentBox);}else{document.id(cell).setStyle('opacity',0.4);}}}if(this.options.title){this.title=new Element('h2',{'class':'lightfaceTitle',html:this.options.title}).inject(this.contentBox);if(this.options.draggable&&window['Drag']!=null){this.draggable=true;new Drag(this.box,{handle:this.title});this.title.addClass('lightfaceDraggable');}}this.messageBox=new Element('div',{'class':'lightfaceMessageBox',html:this.options.content||'',styles:{height:this.options.height}}).inject(this.contentBox);this.footer=new Element('div',{'class':'lightfaceFooter',styles:{display:'none'}}).inject(this.contentBox);this.overlay=new Element('div',{html:'&nbsp;',styles:{opacity:0},'class':'lightfaceOverlay',tween:{link:'chain',duration:this.options.fadeDuration,onComplete:function(){if(this.overlay.getStyle('opacity')==0)this.box.focus();}.bind(this)}}).inject(this.contentBox);if(!this.options.overlayAll){this.overlay.setStyle('top',(this.title?this.title.getSize().y-1:0));}this.buttons=[];if(this.options.buttons.length){this.options.buttons.each(function(button){this.addButton(button.title,button.event,button.color);},this);}this.focusNode=this.box;return this;},addButton:function(title,clickEvent,color){this.footer.setStyle('display','block');var focusClass='lightfacefocus'+color;var label=new Element('label',{'class':color?'lightface'+color:'',events:{mousedown:function(){if(color){label.addClass(focusClass);var ev=function(){label.removeClass(focusClass);document.id(document.body).removeEvent('mouseup',ev);};document.id(document.body).addEvent('mouseup',ev);}}}});this.buttons[title]=(new Element('input',{type:'button',value:title,events:{click:(clickEvent||this.close).bind(this)}}).inject(label));label.inject(this.footer);return this;},showButton:function(title){if(this.buttons[title])this.buttons[title].removeClass('hiddenButton');return this.buttons[title];},hideButton:function(title){if(this.buttons[title])this.buttons[title].addClass('hiddenButton');return this.buttons[title];},close:function(fast){if(this.isOpen){this.box[fast?'setStyles':'tween']('opacity',0);this.fireEvent('close');this._detachEvents();this.isOpen=false;}return this;},open:function(fast){if(!this.isOpen){this.box[fast?'setStyles':'tween']('opacity',1);if(this.resizeOnOpen)this._resize();this.fireEvent('open');this._attachEvents();(function(){this._setFocus();}).bind(this).delay(this.options.fadeDuration+10);this.isOpen=true;}return this;},_setFocus:function(){this.focusNode.setAttribute('tabIndex',0);this.focusNode.focus();},fade:function(fade,delay){this._ie6Size();(function(){this.overlay.setStyle('opacity',fade||1);}.bind(this)).delay(delay||0);this.fireEvent('fade');return this;},unfade:function(delay){(function(){this.overlay.fade(0);}.bind(this)).delay(delay||this.options.fadeDelay);this.fireEvent('unfade');return this;},_ie6Size:function(){if(this.ie6){var size=this.contentBox.getSize();var titleHeight=(this.options.overlayAll||!this.title)?0:this.title.getSize().y;this.overlay.setStyles({height:size.y-titleHeight,width:size.x});}},load:function(content,title){if(content)this.messageBox.set('html',content);if(title&&this.title)this.title.set('html',title);this.fireEvent('complete');return this;},_attachEvents:function(){this.keyEvent=function(e){if(this.options.keys[e.key])this.options.keys[e.key].call(this);}.bind(this);this.focusNode.addEvent('keyup',this.keyEvent);this.resizeEvent=this.options.constrain?function(e){this._resize();}.bind(this):function(){this._position();}.bind(this);window.addEvent('resize',this.resizeEvent);if(this.options.resetOnScroll){this.scrollEvent=function(){this._position();}.bind(this);window.addEvent('scroll',this.scrollEvent);}return this;},_detachEvents:function(){this.focusNode.removeEvent('keyup',this.keyEvent);window.removeEvent('resize',this.resizeEvent);if(this.scrollEvent)window.removeEvent('scroll',this.scrollEvent);return this;},_position:function(){var windowSize=window.getSize(),scrollSize=window.getScroll(),boxSize=this.box.getSize();this.box.setStyles({left:scrollSize.x+((windowSize.x-boxSize.x)/2),top: getAnonymTop(scrollSize.y+((windowSize.y-boxSize.y)/2), boxSize.y/2) });this._ie6Size();return this;},_resize:function(){var height=this.options.height;if(height=='auto'){var max=window.getSize().y-this.options.pad;if(this.contentBox.getSize().y>max)height=max;}this.messageBox.setStyle('height',height);this._position();},toElement:function(){return this.messageBox;},getBox:function(){return this.box;},destroy:function(){this._detachEvents();this.buttons.each(function(button){button.removeEvents('click');});this.box.dispose();delete this.box;}});LightFace.IFrame=new Class({Extends:LightFace,options:{url:''},initialize:function(options){this.parent(options);if(this.options.url)this.load();},load:function(url,title){this.fade();if(!this.iframe){this.messageBox.set('html','');this.iframe=new IFrame({styles:{width:'100%',height:'100%'},events:{load:function(){this.unfade();this.fireEvent('complete');}.bind(this)},border:0}).inject(this.messageBox);this.messageBox.setStyles({padding:0,overflow:'hidden'});}if(title)this.title.set('html',title);this.iframe.src=url||this.options.url;this.fireEvent('request');return this;}});LightFace.Image=new Class({Extends:LightFace,options:{constrain:true,url:''},initialize:function(options){this.parent(options);this.url='';this.resizeOnOpen=false;if(this.options.url)this.load();},_resize:function(){var maxHeight=window.getSize().y-this.options.pad;var imageDimensions=document.id(this.image).retrieve('dimensions');if(imageDimensions.y>maxHeight){this.image.height=maxHeight;this.image.width=(imageDimensions.x*(maxHeight/imageDimensions.y));this.image.setStyles({height:maxHeight,width:(imageDimensions.x*(maxHeight/imageDimensions.y)).toInt()});}this.messageBox.setStyles({height:'',width:''});this._position();},load:function(url,title){var currentDimensions={x:'',y:''};if(this.image)currentDimensions=this.image.getSize();this.messageBox.set('html','').addClass('lightFaceMessageBoxImage').setStyles({width:currentDimensions.x,height:currentDimensions.y});this._position();this.fade();this.image=new Element('img',{events:{load:function(){(function(){var setSize=function(){this.image.inject(this.messageBox).store('dimensions',this.image.getSize());}.bind(this);setSize();this._resize();setSize();this.unfade();this.fireEvent('complete');}).bind(this).delay(10);}.bind(this),error:function(){this.fireEvent('error');this.image.destroy();delete this.image;this.messageBox.set('html',this.options.errorMessage).removeClass('lightFaceMessageBoxImage');}.bind(this),click:function(){this.close();}.bind(this)},styles:{width:'auto',height:'auto'}});this.image.src=url||this.options.url;if(title&&this.title)this.title.set('html',title);return this;}});LightFace.Request=new Class({Extends:LightFace,options:{url:'',request:{url:false}},initialize:function(options){this.parent(options);if(this.options.url)this.load();},load:function(url,title){var props=(Object.append||$extend)({onRequest:function(){this.fade();this.fireEvent('request');}.bind(this),onSuccess:function(response){this.messageBox.set('html',response);this.fireEvent('success');}.bind(this),onFailure:function(){this.messageBox.set('html',this.options.errorMessage);this.fireEvent('failure');}.bind(this),onComplete:function(){this._resize();this._ie6Size();this.messageBox.setStyle('opacity',1);this.unfade();this.fireEvent('complete');}.bind(this)},this.options.request);if(title&&this.title)this.title.set('html',title);if(!props.url)props.url=url||this.options.url;new Request(props).send();return this;}});LightFace.Static=new Class({Extends:LightFace,options:{offsets:{x:20,y:20}},open:function(fast,x,y){this.parent(fast);this._position(x,y);},_position:function(x,y){if(x==null)return;this.box.setStyles({top:y-this.options.offsets.y,left:x-this.options.offsets.x});}});

/* Api do mootools para drag. */
var Drag=new Class({Implements:[Events,Options],options:{snap:6,unit:'px',grid:false,style:true,limit:false,handle:false,invert:false,preventDefault:false,stopPropagation:false,modifiers:{x:'left',y:'top'}},initialize:function(){var params=Array.link(arguments,{'options':Type.isObject,'element':function(obj){return obj!=null;}});this.element=document.id(params.element);this.document=this.element.getDocument();this.setOptions(params.options||{});var htype=typeOf(this.options.handle);this.handles=((htype=='array'||htype=='collection')?$$(this.options.handle):document.id(this.options.handle))||this.element;this.mouse={'now':{},'pos':{}};this.value={'start':{},'now':{}};this.selection=(Browser.ie)?'selectstart':'mousedown';if(Browser.ie&&!Drag.ondragstartFixed){document.ondragstart=Function.from(false);Drag.ondragstartFixed=true;}this.bound={start:this.start.bind(this),check:this.check.bind(this),drag:this.drag.bind(this),stop:this.stop.bind(this),cancel:this.cancel.bind(this),eventStop:Function.from(false)};this.attach();},attach:function(){this.handles.addEvent('mousedown',this.bound.start);return this;},detach:function(){this.handles.removeEvent('mousedown',this.bound.start);return this;},start:function(event){var options=this.options;if(event.rightClick)return;if(options.preventDefault)event.preventDefault();if(options.stopPropagation)event.stopPropagation();this.mouse.start=event.page;this.fireEvent('beforeStart',this.element);var limit=options.limit;this.limit={x:[],y:[]};var styles=this.element.getStyles('left','right','top','bottom');this._invert={x:options.modifiers.x=='left'&&styles.left=='auto'&&!isNaN(styles.right.toInt())&&(options.modifiers.x='right'),y:options.modifiers.y=='top'&&styles.top=='auto'&&!isNaN(styles.bottom.toInt())&&(options.modifiers.y='bottom')};var z,coordinates;for(z in options.modifiers){if(!options.modifiers[z])continue;var style=this.element.getStyle(options.modifiers[z]);if(style&&!style.match(/px$/)){if(!coordinates)coordinates=this.element.getCoordinates(this.element.getOffsetParent());style=coordinates[options.modifiers[z]];}if(options.style)this.value.now[z]=(style||0).toInt();else this.value.now[z]=this.element[options.modifiers[z]];if(options.invert)this.value.now[z]*=-1;if(this._invert[z])this.value.now[z]*=-1;this.mouse.pos[z]=event.page[z]-this.value.now[z];if(limit&&limit[z]){var i=2;while(i--){var limitZI=limit[z][i];if(limitZI||limitZI===0)this.limit[z][i]=(typeof limitZI=='function')?limitZI():limitZI;}}}if(typeOf(this.options.grid)=='number')this.options.grid={x:this.options.grid,y:this.options.grid};var events={mousemove:this.bound.check,mouseup:this.bound.cancel};events[this.selection]=this.bound.eventStop;this.document.addEvents(events);},check:function(event){if(this.options.preventDefault)event.preventDefault();var distance=Math.round(Math.sqrt(Math.pow(event.page.x-this.mouse.start.x,2)+Math.pow(event.page.y-this.mouse.start.y,2)));if(distance>this.options.snap){this.cancel();this.document.addEvents({mousemove:this.bound.drag,mouseup:this.bound.stop});this.fireEvent('start',[this.element,event]).fireEvent('snap',this.element);}},drag:function(event){var options=this.options;if(options.preventDefault)event.preventDefault();this.mouse.now=event.page;for(var z in options.modifiers){if(!options.modifiers[z])continue;this.value.now[z]=this.mouse.now[z]-this.mouse.pos[z];if(options.invert)this.value.now[z]*=-1;if(this._invert[z])this.value.now[z]*=-1;if(options.limit&&this.limit[z]){if((this.limit[z][1]||this.limit[z][1]===0)&&(this.value.now[z]>this.limit[z][1])){this.value.now[z]=this.limit[z][1];}else if((this.limit[z][0]||this.limit[z][0]===0)&&(this.value.now[z]<this.limit[z][0])){this.value.now[z]=this.limit[z][0];}}if(options.grid[z])this.value.now[z]-=((this.value.now[z]-(this.limit[z][0]||0))%options.grid[z]);if(options.style)this.element.setStyle(options.modifiers[z],this.value.now[z]+options.unit);else this.element[options.modifiers[z]]=this.value.now[z];}this.fireEvent('drag',[this.element,event]);},cancel:function(event){this.document.removeEvents({mousemove:this.bound.check,mouseup:this.bound.cancel});if(event){this.document.removeEvent(this.selection,this.bound.eventStop);this.fireEvent('cancel',this.element);}},stop:function(event){var events={mousemove:this.bound.drag,mouseup:this.bound.stop};events[this.selection]=this.bound.eventStop;this.document.removeEvents(events);if(event)this.fireEvent('complete',[this.element,event]);}});Element.implement({makeResizable:function(options){var drag=new Drag(this,Object.merge({modifiers:{x:'width',y:'height'}},options));this.store('resizer',drag);return drag.addEvent('drag',function(){this.fireEvent('resize',drag);}.bind(this));}});

/*  Métodos do anonym  */
function lighfaceAlert(title, message) {
	lighfaceAlert(title, message, 350, 60);
}

function lighfaceAlert(title, message, width, height) {
	var close = {
		title : 'Close',
		event : function() {
			box.close();
		}
	};
	var options = {
		title : title,
		width : width,
		height : height,
		draggable : false,
		content : '<p style="text-align:center;">' + message + '</p>',
		buttons : [close]
	};
	var box = new LightFace(options);
	box.open();
}

function lighfaceConfirm(title, message, yesText, noText, callback) {
	var ok = {
		title : yesText,
		color : 'blue',
		event : function() {
			box.close();
			callback(true);
		}
	};
	var close = {
		title : noText,
		event : function() {
			box.close();
			callback(false);
		}
	};
	var options = {
		title : title,
		width : 350,
		height : 60,
		draggable : false,
		content : '<p style="text-align:center;">' + message + '</p>',
		buttons : [ok, close]
	};
	var box = new LightFace(options);
	box.open();
}
