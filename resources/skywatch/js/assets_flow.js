/**
 * Created by dpinto on 21/04/2016.
 */

$(window).load(function () {
    var tv = 250;

// instantiate our graph!
    var graph = new Rickshaw.Graph({
        element: document.getElementById("assets_flow_chart"),
        width: 680,
        height: 300,
        renderer: 'line',
        series: new Rickshaw.Series.FixedDuration([{name: 'Ingested'}, {name: 'Outgested'}], undefined, {
            timeInterval: tv,
            maxDataPoints: 100,
            timeBase: new Date().getTime() / 1000
        })
    });

    var legend = new Rickshaw.Graph.Legend({
        graph: graph,
        element: document.getElementById('assets_flow_legend')

    });


    graph.render();


    var ticksTreatment = 'glow';

    var xAxis = new Rickshaw.Graph.Axis.Time({
        graph: graph,
        ticksTreatment: ticksTreatment,
        timeFixture: new Rickshaw.Fixtures.Time.Local()
    });
    xAxis.render();

    var yAxis = new Rickshaw.Graph.Axis.Y({
        graph: graph,
        tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
        ticksTreatment: ticksTreatment
    });

    yAxis.render();

// add some data every so often

    var i = 0;
    var iv = setInterval(function () {

        var data = {};

        var randInt = Math.floor(Math.random() * 10);
        data.Ingested = (Math.sin(i++ / 40) + 4) * (randInt + 10);
        data.Outgested = randInt + 10;

        graph.series.addData(data);
        graph.render();

    }, tv);
});
