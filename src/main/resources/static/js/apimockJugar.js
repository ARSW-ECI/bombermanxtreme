//@author hcadavid

apimockJugar=(function(){
	
	var mockdata=[];

	mockdata["jugadores"]=[
		{nombre:"kvnLOCAL",correo:"kvnLOCAL@local.com",clave:"123local",record:"123",idSalaJugando:-1},
		{nombre:"sergioLOCAL",correo:"sergioLOCAL@local.com",clave:"456local",record:"456",idSalaJugando:-1},
		{nombre:"kevinLOCAL",correo:"kevinLOCAL@local.com",clave:"789local",record:"789",idSalaJugando:-1}
	];

	mockdata["salas"]=Array();
	mockdata["salas"].push(Array(/*jugadores*/));//sala 0
	mockdata["salas"].push(Array(/*jugadores*/));//sala 1

	return {
		getJugadoresDeSala:function(idSala,callback){
			callback(mockdata["salas"][idSala]);
		}
	}	

})();

apiclientJugar = (function () {
	return {
		getJugadoresDeSala:function(idSala,callback){
			$.ajax({
				url: "http://localhost:8080/sala/"+idSala+"/Jugadores",
				type: "GET",
			}).done(function(data) {
				callback(data);
			}).fail(function( jqXHR, textStatus ) {
				callback(undefined);
				if(jqXHR.status!=404)
					alert("Error "+jqXHR.status+" peticion GET!");
			});
		}
	}	
})();