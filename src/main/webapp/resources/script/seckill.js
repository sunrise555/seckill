//存放主要交互逻辑的js代码
//模块化：分包,jso

var seckill = {
		//封装秒杀相关ajax的URL
		URL : {
			now:function(){
				return '/seckill/time/now';
			},
			exposer:function(seckillId){
				return '/seckill/' + seckillId + '/exposer';
			},
			execution:function(seckillId, md5){
				return '/seckill/' + seckillId + '/' + md5 + '/execution';
			}
			
		},
		//判断手机号
		validatePhone : function(phone){
			if (phone && phone.length == 11 && !isNaN(phone)) {
				return true;
			}else{
				return false;
			}
		},
		
		//处理秒杀逻辑的函数
		handleSeckillKill: function(seckillId, node){
			//放置秒杀
			node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
			$.post(seckill.URL.exposer(seckillId),{},function(result){
				//result中是执行seckill.URL.exposer()地址对应的controller时返回的json数据
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						//开启秒杀
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl = seckill.URL.execution(seckillId, md5);
						console.log('killUrl:'+killUrl);
						//拿到秒杀地址后就可以给前面的按钮注册动作
						//.one('click')表示只绑定点击动作一次，即用户在秒杀时可能会多次点击按钮，使用one可以保证只传递URL传一次
						//.click()方法表示一直绑定
						$('#killBtn').one('click',function(){
							//执行秒杀请求
							//1、先禁用按钮
							$(this).addClass('disabled');
							//2、发送秒杀请求
							$.post(killUrl,{},function(result){
								if(result && result['success']){
									var killResult = result['data'];
									var state = killResult['state'];
									var stateInfo = killResult['stateInfo'];
									//3、显示秒杀结果
									node.html('<span class="label label-success">' + stateInfo + '</span>')
								}
							});
						});
						node.show();
					}else{
						//未开始秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						//重新计算计时逻辑
						seckill.countdown(seckillId, start, end, now);
					}
				}else{
					console.log('result:'+result);
				}
			});
		},
		//设置倒计时页面
		countdown : function(seckillId, startTime, endTime, nowTime){
			var seckillbox = $('#seckill-box');
			if(nowTime > endTime){
				seckillbox.html('秒杀结束');
			}else if(nowTime <startTime){//秒杀未开始，给出计时页面
				
				var killTime = new Date(startTime + 1000);//加上1s，防止用户事件偏移
				//jquery.countdown.min.js countdown函数提供一个计时事件的绑定
				seckillbox.countdown(killTime, function(event){
					//设置时间格式
					var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
					seckillbox.html(format);
				}).on('finish.countdown', function(){//倒计时完成后回调事件
					//获取秒杀地址，出现执行秒杀的按钮操作
					seckill.handleSeckillKill(seckillId, seckillbox);
				});
			}else{
				//秒杀开始
				seckill.handleSeckillKill(seckillId, seckillbox)
			}
		},
		//详情页秒杀逻辑
		detail : {
			//详情页面初始化
			init : function(params) {
				//用户手机验证，计时交互
				//这里先暂时在前端完成手机号的存储，存在cookie中，实际开发中由后端存入数据库
				var  killPhone = $.cookie('killPhone');
				var  startTime = params['startTime'];
				var  endTime = params['endTime'];
				var  seckillId = params['seckillId'];
				//验证手机号
				if(!seckill.validatePhone(killPhone)) {
					//弹出绑定手机号的悬浮层,控制输出
					var killPhoneModal = $("#killPhoneModal");
					killPhoneModal.modal({
						show : true ,//显示弹出层
						backdrop : 'static', //静止位置关闭
						keyboard : false
					});
					$('#killPhoneBtn').click(function(){
						var inputPhone = $('#killphoneKey').val();
						if(seckill.validatePhone(inputPhone)) {
							//将电话写入cookie
                            var millisecond = new Date().getTime();
                            var expiresTime = new Date(millisecond + 60 * 1000 * 1);
                            // 设置cookie 1分钟失效
							$.cookie('killPhone', inputPhone,{expires:expiresTime,path:'/seckill'});
							//刷新页面
							window.location.reload();
						}else{
							$('#killphoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
						}
					});
				}
				
				//用户已经登录
				//计时交互
				$.get(seckill.URL.now(), {}, function(result){
					if(result && result['success']){
						var nowTime = result['data'];
						//判断当前是否在秒杀时间段中，抽取一个函数countdown
						seckill.countdown(seckillId, startTime, endTime, nowTime);
					}else{
						console.log("result:"+result);
					}
				});
							
			}
		}
}
