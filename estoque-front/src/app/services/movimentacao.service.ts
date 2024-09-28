import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movimentacao } from '../models/movimentacao.model';


@Injectable({
  providedIn: 'root',
})

export class MovimentacaoService {
  private apiUrl = 'http://localhost:8080/movimentacoes'; // URL da API

  constructor(private http: HttpClient) {}


  // Método para consultar a lista de produtos
  consultarMovimentacoes(): Observable<Movimentacao[]> {
    return this.http.get<Movimentacao[]>(this.apiUrl);
  }
}