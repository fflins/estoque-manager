import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Produto } from '../models/produto.model';

@Injectable({
  providedIn: 'root',
})
export class ProdutoService {
  private apiUrl = 'http://localhost:8080/produtos'; // URL da API

  constructor(private http: HttpClient) {}

  // Método para inserir um produto
  inserirProduto(produto: Produto): Observable<any> {
    return this.http.post<any>(this.apiUrl, produto);
  }

  // Método para consultar a lista de produtos
  consultarProdutos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.apiUrl);
  }

  // Método para remover um produto pelo código
  removerProduto(codigo: string, quantidade: number): Observable<any> {
    const options = {
      headers: { 'Content-Type': 'application/json' },
      body: { quantidade },
      observe: 'response' as 'body' // Garante que a resposta completa seja capturada
    };
    return this.http.delete(`${this.apiUrl}/${codigo}`, options);
  }
  
  
}
