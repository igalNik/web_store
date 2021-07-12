var mounthAppearence = {};

for (var i=0 ; i<12 ; i++){
    var mounth = new Date();
    mounth.setMonth(mounth.getMonth()-i);
    mounthAppearence[mounth.toLocaleString('default', { month: 'short' })] = 0;
}
