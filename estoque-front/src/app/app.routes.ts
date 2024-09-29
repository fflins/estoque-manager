import { Routes } from '@angular/router';
import { MenuInicialComponent } from './components/menu-inicial/menu-inicial.component';
import { InserirProdutoComponent } from './components/inserir-produto/inserir-produto.component';
import { ConsultarProdutosComponent } from './components/consultar-produtos/consultar-produtos.component';
import { RemoverProdutoComponent } from './components/remover-produto/remover-produto.component';
import { ConsultarMovimentacaoComponent } from './components/consultar-movimentacao/consultar-movimentacao.component';

export const routes: Routes = [
  { path: '', component: MenuInicialComponent }, // Rota para o menu inicial
  { path: 'inserir-produto', component: InserirProdutoComponent },
  { path: 'consultar-produtos', component: ConsultarProdutosComponent },
  { path: 'remover-produto', component: RemoverProdutoComponent },
  { path: 'consultar-movimentacoes', component: ConsultarMovimentacaoComponent },
];
