//Register required Template7 helpers, before templates compilation
Template7.registerHelper('dayOfWeek', function (date) {
    date = new Date(date);
    var days = ('Sunday Monday Tuesday Wednesday Thursday Friday Saturday').split(' ');
    return days[date.getDay()];
});

Template7.registerHelper('formatedDated', function (date) {
    date = new Date(date);
    var months = 'Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec'.split(' ');
    return months[date.getMonth()] + ' ' + date.getDate() + ' ' + date.getFullYear();
});
