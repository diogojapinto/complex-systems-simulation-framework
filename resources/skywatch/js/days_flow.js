/**
 * Created by dpinto on 21/04/2016.
 */

$(window).load(function() {
    var palette = new Rickshaw.Color.Palette( { scheme: 'httpStatus' } );

    var wrapper = new Rickshaw.Graph.Ajax( {
        element: document.getElementById("days_flow_chart"),
        dataURL: 'data/status.json',
        width: 400,
        height: 300,
        renderer: 'bar',
        onData: function(d) { return transformData(d) },
        onComplete: function(w) {
            var legend = new Rickshaw.Graph.Legend( {
                element: document.querySelector('#days_flow_legend'),
                graph: w.graph
            } );
        }
    } );

    function transformData(d) {
        var data = [];
        var statusCounts = {};

        Rickshaw.keys(d).sort().forEach( function(t) {
            Rickshaw.keys(d[t]).forEach( function(status) {
                statusCounts[status] = statusCounts[status] || [];
                statusCounts[status].push( { x: parseFloat(t), y: d[t][status] } );
            } );
        } );

        Rickshaw.keys(statusCounts).sort().forEach( function(status) {
            data.push( {
                name: status,
                data: statusCounts[status],
                color: palette.color(status)
            } );
        } );

        Rickshaw.Series.zeroFill(data);
        return data;
    }
});