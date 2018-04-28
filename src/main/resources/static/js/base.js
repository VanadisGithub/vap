//ajax加载动画
$(document).ajaxStart(function () {
    layer.ready(function () {
        layer.load(1, {shade: 0.1})
    })
});
$(document).ajaxStop(function () {
    layer.close(layer.load());
});

function errorMsg(msg) {
    if (msg == null) {
        layer.msg("访问出错~！");
    } else {
        layer.msg(msg);
    }
}