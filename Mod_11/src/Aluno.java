//package Mod_11.src;
//
//
//import java.util.Objects;
//
//public class Aluno implements Comparable<Aluno>{
//
//    private String nome;
//    private String curso;
//    private double nota;
//    private String sala;
//
//    public Aluno(String nome, String curso, double nota) {
//        this.nome = nome;
//        this.curso = curso;
//        this.nota = nota;
//    }
//
//    public Aluno(String nome, String curso, double nota, String sala) {
//        this(nome, curso, nota);
//        this.sala = sala;
//    }
//
//    public String getNome() {
//        return nome;
//    }
//
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//
//    public String getCurso() {
//        return curso;
//    }
//
//    public void setCurso(String curso) {
//        this.curso = curso;
//    }
//
//    public double getNota() {
//        return nota;
//    }
//
//    public void setNota(double nota) {
//        this.nota = nota;
//    }
//
//    public String getSala() {
//        return sala;
//    }
//
//    public void setSala(String sala) {
//        this.sala = sala;
//    }
//
////    @Override
////    public String toString() {
////        return "Aluno{" +
////                "nome='" + nome + '\'' +
////                ", curso='" + curso + '\'' +
////                ", nota=" + nota +
////                ", sala='" + sala + '\'' +
////                '}';
////    }
//
//    /**
//     * Usado para converter o objeto em String
//     *
//     * @return
//     */
//    public String toString() {
//        return this.nome;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        Aluno other = (Aluno) obj;
//        if (nome == null) {
//            if (other.nome != null)
//                return false;
//        } else if (!nome.equals(other.nome))
//            return false;
//        return true;
//    }
//
//
//
//
//}